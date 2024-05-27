package com.restservice.authentication.dto;

import com.restservice.authentication.model.Household;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseholdDTO {

    private Long id;
    private String name;
    private String address;
    private String country;
    private String city;
    private String description;

    public HouseholdDTO (Household household) {
        this.id = household.getId();
        this.name = household.getName();
        this.address = household.getAddress();
        this.country = household.getCountry();
        this.city = household.getCity();
        this.description = household.getDescription();
    }
}
