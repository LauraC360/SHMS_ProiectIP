package org.example;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "products", schema = "sql11703727")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String name;

    @Column
    Integer category;

    public Integer getId() {
        return id;
    }
}