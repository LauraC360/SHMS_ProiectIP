package com.restservice.authentication.service;

import com.restservice.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.restservice.authentication.model.User;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Loading user details for user: " + email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = userRepository.findByUsername(email);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
        }
        return new CustomUserDetail(user);
    }
}
