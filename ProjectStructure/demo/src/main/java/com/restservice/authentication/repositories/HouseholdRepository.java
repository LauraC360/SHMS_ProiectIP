package com.restservice.authentication.repositories;

import com.restservice.authentication.model.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {

    @Query("SELECT h FROM Household h JOIN FETCH h.users u WHERE u.id = :userId")
    Optional<Household> findByUserId(@Param("userId") Long userId);
}
