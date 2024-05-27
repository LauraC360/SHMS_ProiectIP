package com.restservice.authentication.controller;

import com.restservice.authentication.dto.HouseholdDTO;
import com.restservice.authentication.model.Household;
import com.restservice.authentication.model.User;
import com.restservice.authentication.service.AuthenticationService;
import com.restservice.authentication.service.HouseholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/household")
public class HouseholdController {

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
        User actualUser = authService.findByEmail(user.getEmail());
        if (householdService.findByUser(actualUser) != null) {
            HouseholdDTO householdDTO = new HouseholdDTO(householdService.findByUser(actualUser));
            return ResponseEntity.ok().body(householdDTO);
        }
        else {
            return getBadRequestMessage("Household with id ", actualUser.getId());
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
}
