package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceProductRepository extends JpaRepository<PriceProduct, Integer> {

    PriceProduct findById(PriceProductId priceProductId);
}
