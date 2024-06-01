package com.restservice.authentication.model;
import com.restservice.authentication.dto.HouseholdDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "household")
public class Household {

    @Id
    @Column(name = "household_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "description")
    private String description;

    @OneToMany
    @JoinTable(name = "household_users",
            joinColumns = @JoinColumn(name = "household_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        if (user == null) {
            return;
        }
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public Household(HouseholdDTO householdDTO) {
        this.name = householdDTO.getName();
        this.address = householdDTO.getAddress() + ", " + householdDTO.getCity();
        this.country = householdDTO.getCountry();
        this.city = householdDTO.getCity();
        this.description = householdDTO.getDescription();
    }

    public boolean removeUser(Integer userID) //returns true if remove was successful, false otherwise
    {
        if (userID == null) {
            return false;
        }
        if (users == null) {
            users = new ArrayList<>();
        }
        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getId() == userID.longValue()) {
                users.remove(i);
                return true;
            }
        return false;
    }
}
