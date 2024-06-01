package com.restservice.authentication.repositories;

import com.restservice.authentication.model.User;
import com.restservice.authentication.model.UserAllergens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAllergensRepository extends JpaRepository<UserAllergens, Long> {
    List<UserAllergens> findByUser(User user);
    void deleteByUser(User user);

}