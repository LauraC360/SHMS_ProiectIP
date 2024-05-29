package com.restservice.restockAndShoppingOptimization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer>{

    Products findByCategoryAndName(int category, String name);
    Products findById(int id);
    Products findByName(String name);

    List<Products> findAllByCategoryAndNameContaining(int category, String name);

}
