package org.example;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Embeddable
public class PriceProductId implements Serializable {
    private Integer id_product;
    private Integer id_store;

    public PriceProductId() {
    }

    public PriceProductId(Integer storeId, Integer productId) {
        this.id_store = storeId;
        this.id_product = productId;
    }

    public void setId(Integer id) {
        this.id_product = id;
    }

    public void setIdStore(int i) {
        this.id_store = i;
    }
    public Integer getId() {
        return id_product;
    }

    public Integer getIdStore() {
        return id_store;
    }
}
