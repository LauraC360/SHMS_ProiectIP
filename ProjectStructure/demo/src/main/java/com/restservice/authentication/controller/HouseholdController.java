package com.restservice.authentication.controller;

import com.restservice.authentication.dto.HouseholdDTO;
import com.restservice.authentication.dto.UserDTO;
import com.restservice.authentication.model.Household;
import com.restservice.authentication.model.User;
import com.restservice.authentication.service.AuthenticationService;
import com.restservice.authentication.service.HouseholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/household")
public class HouseholdController {

    private String emailUserSession = null;

    @Autowired
    HouseholdService householdService;

    @Autowired
    AuthenticationService authService;

    private static ResponseEntity<String> getBadRequestMessage(String message, Long id) {
        return ResponseEntity
                .badRequest()
                .body(message + id + " not found.");
    }

    @GetMapping("/get-house")
    public ResponseEntity<?> getAll(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (emailUserSession == null) {
            emailUserSession = user.getEmail();
        }

        if (user == null || user.getEmail() == null) {
            user = authService.findByEmail(emailUserSession);
            if (user == null || user.getEmail() == null)
                return ResponseEntity.badRequest().body(null);
            session.setAttribute("user", user);
        }

        //System.out.println(user);
        User actualUser = authService.findByEmail(user.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found in database");
        }
        if (householdService.findByUser(actualUser) != null) {
            HouseholdDTO householdDTO = new HouseholdDTO(householdService.findByUser(actualUser));
            return ResponseEntity.ok().body(householdDTO);
        }
        else {
            return getBadRequestMessage("Household with user id ", actualUser.getId());
        }
    }

    @PostMapping("/add-house")
    public ResponseEntity<?> createHousehold(@RequestBody HouseholdDTO householdDTO, HttpSession session) throws IOException {
        try {
            Household household = new Household(householdDTO);
            User user = (User) session.getAttribute("user");
            User actualUser = authService.findByEmail(user.getEmail());
            return ResponseEntity.ok().body(householdService.addHousehold(household, actualUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/load-users")
    public ResponseEntity<?> loadUsers(HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        User actualUser = authService.findByEmail(user.getEmail());
        List<UserDTO> userDTOList = householdService.memberList(actualUser);
        if (userDTOList == null) {
            return getBadRequestMessage("Household with user id ", actualUser.getId());
        }
        return ResponseEntity.ok().body(userDTOList);
    }

    @DeleteMapping("/remove-house")
    public ResponseEntity<?> removeHousehold(HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            User actualUser = authService.findByEmail(user.getEmail());
            householdService.removeHousehold(actualUser.getId());
            return ResponseEntity.ok("Household deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(
            name="/kick-user",
            value = {"kick-user/{id_user}"})
    public ResponseEntity<?> kickUser(HttpSession session, Integer id_user){
        try {
            User user = (User) session.getAttribute("user");
            User actualUser = authService.findByEmail(user.getEmail());
            householdService.kickUser(actualUser, id_user);
            return ResponseEntity.ok("User kicked");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
