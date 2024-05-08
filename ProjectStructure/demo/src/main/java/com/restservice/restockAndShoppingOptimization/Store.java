package org.example;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "Stores", schema = "sql11703727")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_store")
    private Integer id_store;

    @Column(name = "name")
    private String name;

    public Integer getId() {
        return id_store;
    }

    public String getName() {
        return name;
    }
}