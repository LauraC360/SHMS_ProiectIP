package com.restservice.authentication.service;

import com.restservice.authentication.dto.UserDTO;
import com.restservice.authentication.model.Household;
import com.restservice.authentication.model.User;
import com.restservice.authentication.repositories.HouseholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseholdService {

    @Autowired
    HouseholdRepository householdRepository;

    @Autowired
    GeocodingService geocodingService;

    public Household findById(Long id) {
        return householdRepository.findById(id)
                .orElse(null);
    }

    public Household addHousehold(Household household, User user) throws Exception {
        GeocodingService.GeocodingResult result = geocodingService.geocode(household.getAddress());
        household.setLatitude(result.getLat());
        household.setLongitude(result.getLon());
        household.addUser(user);
        return householdRepository.save(household);
    }

    public Household inviteUser(Household household, UserDTO userDTO) {
        User user = new User(userDTO);
        household.addUser(user);
        return householdRepository.save(household);
    }

    public List<Household> findAll() {
        return householdRepository.findAll();
    }

    public Household findByUser(User user) {
        Long userId = user.getId();
        return householdRepository.findByUserId(userId)
                .orElse(null);
    }
}
