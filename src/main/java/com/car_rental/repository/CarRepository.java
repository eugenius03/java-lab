package com.car_rental.repository;

import com.car_rental.model.Car;

public class CarRepository{

    private final GenericRepository<Car> repository;

    public CarRepository(){
        repository = new GenericRepository<>(Car::getLicensePlate, "Car");
    }

    
}
