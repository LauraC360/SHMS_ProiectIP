package com.restservice.authentication.service;

import com.restservice.authentication.dto.UserDTO;
import com.restservice.authentication.model.Household;
import com.restservice.authentication.model.Role;
import com.restservice.authentication.model.User;
import com.restservice.authentication.repositories.HouseholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<UserDTO> memberList(User user) {
        Long userId = user.getId();
        Household household = householdRepository.findByUserId(userId)
                .orElse(null);
        if (household == null) {
            return null;
        }
        return household.getUsers().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public void removeHousehold(Long userID) throws Exception{
        Optional<Household> householdOptional=householdRepository.findByUserId(userID);
        if(householdOptional.isEmpty())
            throw new Exception("User id does not exits");
        Household household= householdOptional.get();
        householdRepository.delete(household);
    }

    public void kickUser(User admin, Integer kickedUser)throws Exception{
        if(admin.getRole()!= Role.ADMIN)
            throw new Exception("Only admins can kick users");
        Household household = householdRepository.findByUserId(admin.getId())
                .orElse(null);
        if(household==null)
            throw new Exception("Household id doesn't exist");
        if(!household.removeUser(kickedUser))
            throw new Exception("User isn't part of admin's household");
        householdRepository.save(household);
    }
}
