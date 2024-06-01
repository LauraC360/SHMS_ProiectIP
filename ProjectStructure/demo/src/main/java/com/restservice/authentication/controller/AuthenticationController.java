package com.restservice.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restservice.authentication.model.*;
import com.restservice.authentication.repositories.UserAllergensRepository;
import com.restservice.authentication.repositories.UserPreferencesRepository;
import com.restservice.authentication.repositories.UserRepository;
import com.restservice.authentication.sendgrid.EmailService;
import com.restservice.authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Base64;

@CrossOrigin
@Controller
public class AuthenticationController {

    private String emailUserSession = null;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AuthenticationService authService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPreferencesRepository userPreferencesRepository;
    @Autowired
    private UserAllergensRepository userAllergensRepository;

    /*@PostMapping("/register2")
    public ResponseEntity<AuthenticationResponse> register(

            @RequestBody User request
    ){
        //System.out.println("Hello From secured url");
        return ResponseEntity.ok(authService.register(request));
    }*/

    /*@PostMapping("/login2")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ){
        return ResponseEntity.ok(authService.authenticate(request));
    }*/

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request, HttpSession session) {
        AuthenticationResponse response = authService.authenticate(request);
        if (response != null && response.getToken() != null) {
            session.setAttribute("user", request);
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }


    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user, HttpSession session) {
        try {
            authService.checkUser(user);
            session.setAttribute("user", user);
            System.out.println(user);
            return ResponseEntity.ok().body(new AuthenticationResponse("Redirect to /validation"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @ResponseBody
    @PostMapping("/validation")
    public ResponseEntity<String> validateCode(@RequestBody Map<String, String> payload, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String code = payload.get("code");
        int num = Integer.parseInt(code);
        if (num == emailService.getCode()) {
            try {
                authService.save(user);
                return ResponseEntity.ok("Registered successfully!");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.badRequest().body("Invalid code");
    }

    @PostMapping("/account-security")
    public ResponseEntity<String> accountSecurity(@RequestPart("file") MultipartFile file, @RequestPart("user") String userJson, @RequestPart("oldPassword") String oldPassword, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            User newUser = new ObjectMapper().readValue(userJson, User.class);
            // Aici puteți salva sau procesa fișierul cum doriți
            // De exemplu, puteți converti fișierul într-un array de byte și să îl setați ca imagine de profil pentru newUser
            newUser.setPhoto(file.getBytes());
            User newSession = authService.updateUser(user, newUser, oldPassword);
            session.removeAttribute("user");
            session.setAttribute("user", newSession);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update error");
        }
    }

    @ResponseBody
    @PostMapping("/allergenspreferences")
    @Transactional
    public ResponseEntity<String> updateAllergensAndPreferences(@RequestBody Map<String, Object> payload, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || sessionUser.getEmail() == null) {
            return ResponseEntity.badRequest().body("User not found in session or user ID is null");
        }

        // Recuperarea utilizatorului din baza de date
        User user = userRepository.findByEmail(sessionUser.getEmail());

        if (user == null) {
            throw new RuntimeException("User not found in database");
        }

        // Stergerea datelor existente
        userAllergensRepository.deleteByUser(user);
        userPreferencesRepository.deleteByUser(user);

        // Adaugarea alergenilor
        List<String> allergens = (List<String>) payload.get("allergens");
        for (String allergen : allergens) {
            UserAllergens userAllergen = new UserAllergens();
            userAllergen.setUser(user);
            userAllergen.setAllergens(allergen);
            userAllergensRepository.save(userAllergen);
        }

        // Adaugarea preferintelor
        List<String> preferences = (List<String>) payload.get("preferences");
        for (String preference : preferences) {
            UserPreferences userPreference = new UserPreferences();
            userPreference.setUser(user);
            userPreference.setPreferences(preference);
            userPreferencesRepository.save(userPreference);
        }

        List<UserAllergens> userAllergens = userAllergensRepository.findAll();
        for (int i = 0; i < userAllergens.size(); i++) {
            jdbcTemplate.update("UPDATE user_allergens SET id = ? WHERE id = ?", i + 1, userAllergens.get(i).getId());
        }

        List<UserPreferences> userPreferences = userPreferencesRepository.findAll();
        for (int i = 0; i < userPreferences.size(); i++) {
            jdbcTemplate.update("UPDATE user_preferences SET id = ? WHERE id = ?", i + 1, userPreferences.get(i).getId());
        }

        jdbcTemplate.execute("ALTER TABLE user_allergens AUTO_INCREMENT = " + (userAllergens.size() + 1));
        jdbcTemplate.execute("ALTER TABLE user_preferences AUTO_INCREMENT = " + (userPreferences.size() + 1));

        return ResponseEntity.ok("Allergens and preferences updated successfully");
    }

    @ResponseBody
    @GetMapping("/allergenspreferences")
    public ResponseEntity<Map<String, List<String>>> getAllergensAndPreferences(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        if (emailUserSession == null) {
            emailUserSession = sessionUser.getEmail();
        }

        if (sessionUser == null || sessionUser.getEmail() == null) {
            sessionUser = userRepository.findByEmail(emailUserSession);
            if (sessionUser == null || sessionUser.getEmail() == null)
                return ResponseEntity.badRequest().body(null);
            session.setAttribute("user", sessionUser);
        }

        // Recuperarea utilizatorului din baza de date
        User user = userRepository.findByEmail(sessionUser.getEmail());

        if (user == null) {
            throw new RuntimeException("User not found in database");
        }

        List<UserAllergens> userAllergens = userAllergensRepository.findByUser(user);
        List<UserPreferences> userPreferences = userPreferencesRepository.findByUser(user);

        List<String> allergens = userAllergens.stream().map(UserAllergens::getAllergens).collect(Collectors.toList());
        List<String> preferences = userPreferences.stream().map(UserPreferences::getPreferences).collect(Collectors.toList());

        Map<String, List<String>> response = new HashMap<>();
        response.put("allergens", allergens);
        response.put("preferences", preferences);
        return ResponseEntity.ok(response);
    }


    /*@PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request) {
        return ResponseEntity.ok(authService.register(request));
    }*/

    @ResponseBody
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        System.out.println("Hello From secured url");
        return ResponseEntity.ok("Hello From secured url");
    }

    @GetMapping("/admin_only")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("Hello From Admin only url");
    }


}
