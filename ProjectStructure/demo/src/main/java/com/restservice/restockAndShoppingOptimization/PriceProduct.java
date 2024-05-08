package org.example;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpHeaders;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "Price_products", schema = "sql11703727")
public class PriceProduct {
    @EmbeddedId
    private PriceProductId id;

    @Column(name = "Price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_store", referencedColumnName = "id_store", insertable = false, updatable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", referencedColumnName = "id", insertable = false, updatable = false)
    private Products product;

    public PriceProduct() {}

    public PriceProduct(Integer storeId, Integer productId, Double price) {
        this.id = new PriceProductId(storeId, productId);
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public PriceProductId getId() {
        return id;
    }

    public Store getStore() {
        return store;
    }
}