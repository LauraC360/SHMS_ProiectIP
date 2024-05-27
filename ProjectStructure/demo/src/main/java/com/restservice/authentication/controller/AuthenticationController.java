package com.restservice.authentication.controller;

import com.restservice.authentication.model.AuthenticationResponse;
import com.restservice.authentication.model.UpdateUserRequest;
import com.restservice.authentication.model.User;
import com.restservice.authentication.sendgrid.EmailService;
import com.restservice.authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@CrossOrigin
@Controller
public class AuthenticationController {

    private AuthenticationService authService;
    private EmailService emailService;



    public AuthenticationController(AuthenticationService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request, HttpSession session){
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

    @ResponseBody
    @PostMapping("/account-security")
    public ResponseEntity<String> accountSecurity(@RequestBody UpdateUserRequest request, HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            String oldPassword = request.getOldPassword();
            User newUser = request.getUser();
            System.out.println(user);
            User newSession = authService.updateUser(user,newUser,oldPassword);
            session.removeAttribute("user");
            session.setAttribute("user", newSession);
            return ResponseEntity.ok("User updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Update error");
        }
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
