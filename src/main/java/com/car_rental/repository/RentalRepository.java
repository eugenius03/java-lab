package com.car_rental.repository;

import com.car_rental.model.Rental;

public class RentalRepository {
    private final GenericRepository<Rental> repository;

    public RentalRepository(){
        repository = new GenericRepository<>(rental -> String.valueOf(rental.getId()), "Rental");
    }
}
