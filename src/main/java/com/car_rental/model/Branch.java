package com.car_rental.model;

import java.util.Comparator;

import com.car_rental.util.ValidationUtil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Branch (
    
    @NotBlank(message = "Branch name cannot be null or blank")
    @Size(min = 1, max = 100, message = "Branch name must be 1-100 characters long")
    String name,

    @NotBlank(message = "Branch location cannot be null or blank")
    @Size(min = 1, max = 100, message = "Branch location must be 1-100 characters long")
    String location
    ) implements Comparable<Branch> {
    
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Branch.class);

    public Branch(String name, String location) {       
        this.name = name;
        this.location = location; 
        ValidationUtil.validate(this);

        logger.info("Branch created: {} at {}", name, location);
    }

    @Override
    public int compareTo (Branch o){
        return this.name().compareTo(o.name());
    }

    public static Comparator<Branch> byLocation(){
        return Comparator.comparing(Branch::location);
    }


}
