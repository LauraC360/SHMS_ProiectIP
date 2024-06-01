package com.restservice.authentication.controller;

import com.restservice.authentication.dto.UserDTO;
import com.restservice.authentication.model.User;
import com.restservice.authentication.model.UserAllergens;
import com.restservice.authentication.model.UserPreferences;
import com.restservice.authentication.repositories.UserRepository;
import com.restservice.authentication.sendgrid.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
public class ViewController {

    @Autowired
    private UserRepository userRepository;
    private EmailService emailService;

    public ViewController(EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping("/validation")
    public String getValidationCode(HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println("User: " + user);
        emailService.setUser(user);
        String response = emailService.sendEmail();
        System.out.println("Response from SendGrid: " + response);
        return "validation";
    }

    @GetMapping("/account-security")
    @ResponseBody
    public UserDTO account(HttpSession session) {
        User user = (User) session.getAttribute("user");
        User actualUser = userRepository.findByEmail(user.getEmail());
        System.out.println("User: " + actualUser);
        if (user != null) {
            UserDTO userDTO = new UserDTO(actualUser);
            return userDTO;
        }
        return null;
    }
}