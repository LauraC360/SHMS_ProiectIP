package org.example;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "locations", schema = "sql11703727")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_location;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "id_store")
    private Store id_store;

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
