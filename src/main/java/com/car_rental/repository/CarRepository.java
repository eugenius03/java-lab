package com.car_rental.repository;

import java.util.List;

import com.car_rental.model.Car;

public class CarRepository extends GenericRepository<Car>{

    public CarRepository(){
        super(Car::getLicensePlate, "Car");
    }

    public List<Car> sortByYear(){
        return sortByComparator(Car.byYear());
    }

    public List<Car> sortByMileage(){
        return sortByComparator(Car.byMileage());
    }

    
}
