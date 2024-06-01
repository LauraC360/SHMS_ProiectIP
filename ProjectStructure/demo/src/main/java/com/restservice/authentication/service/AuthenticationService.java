package com.restservice.authentication.service;

import com.restservice.authentication.model.AuthenticationResponse;
import com.restservice.authentication.model.Role;
import com.restservice.authentication.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.restservice.authentication.model.User;

import java.util.Optional;

@Service
public class AuthenticationService {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user = repository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = repository.findByEmail(request.getEmail());
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public void checkUser(User user) throws RuntimeException {
        System.out.println("Checking user");
        User existingUser = repository.findByEmail(user.getEmail());
        if (existingUser != null && existingUser.getId() != user.getId()) {
            System.out.println("Checking user already exists");
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        existingUser = repository.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getId() != user.getId()) {
            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
        }
    }

    public User save(User user) throws RuntimeException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);
        User existingUser = repository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        existingUser = repository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
        }
        return repository.save(user);
    }

    public User updateUser(User oldUser, User newUser, String oldPassword) throws RuntimeException {
        User existingUser = repository.findByEmail(oldUser.getEmail());
        System.out.println();
        System.out.println(existingUser.getPhoto());
        if (passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            if (!(newUser.getUsername().isEmpty())) {
                existingUser.setUsername(newUser.getUsername());
            }
            if (!(newUser.getEmail().isEmpty())) {
                existingUser.setEmail(newUser.getEmail());
            }
            if (!(newUser.getPassword().isEmpty())) {
                existingUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            }
            if (newUser.getPhoto() != null) {
                existingUser.setPhoto(newUser.getPhoto());
            }
        }
        else {
            throw new RuntimeException("Current password incorrect");
        }
        System.out.println(oldUser);
        System.out.println(existingUser);
        System.out.println(oldPassword);
        checkUser(existingUser);
        return repository.save(existingUser);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }


}
