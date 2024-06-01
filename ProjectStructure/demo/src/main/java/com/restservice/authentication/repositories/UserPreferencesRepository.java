package com.restservice.authentication.repositories;

import com.restservice.authentication.model.User;
import com.restservice.authentication.model.UserAllergens;
import com.restservice.authentication.model.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
    List<UserPreferences> findByUser(User user);
    void deleteByUser(User user);
}