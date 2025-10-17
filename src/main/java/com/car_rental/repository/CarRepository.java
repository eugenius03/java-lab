package com.car_rental.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.model.Car;

public class CarRepository extends GenericRepository<Car>{
    private static final Logger logger = LoggerFactory.getLogger(CarRepository.class);


    public CarRepository(){
        super(Car::getLicensePlate, "Car");
    }

    public List<Car> sortByYear(){
        return sortByComparator(Car.byYear());
    }

    public List<Car> sortByMileage(){
        return sortByComparator(Car.byMileage());
    }

    public Optional<Car> findByLicensePlate(String licensePlate){
        if(licensePlate == null){
            logger.warn(String.format("findByLicensePlate called with null licensePlate"));
            return Optional.empty();
        }
        logger.info(String.format("Trying to find Car by %s", licensePlate));
        return findByIdentity(licensePlate);
    }

    public List<Car> findByModel(String model){
        if(model == null){
            logger.warn(String.format("findByModel called with null model"));
            return Collections.emptyList();
        }
        logger.info(String.format("Trying to find Car by %s", model));
        return findByPredicate(x -> x.getModel().trim().toLowerCase().contains(model.trim().toLowerCase()))
            .toList();
    }
    
}
