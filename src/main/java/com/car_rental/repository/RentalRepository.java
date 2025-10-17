package com.car_rental.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.model.Rental;

public class RentalRepository  extends GenericRepository<Rental>{
    private static final Logger logger = LoggerFactory.getLogger(RentalRepository.class);


    public RentalRepository(){
        super(rental -> String.valueOf(rental.getId()), "Rental");
    }

    public List<Rental> sortByStartDate(){
        return sortByComparator(Rental.byStartDate());
    }

    public List<Rental> sortByEndDate(){
        return sortByComparator(Rental.byEndDate());
    }

    public Optional<Rental> findById(String id){
        if(id == null){
            logger.warn(String.format("findById called with null id"));
            return Optional.empty();
        }
        logger.info(String.format("Trying to find Rental by %s", id));
        return findByIdentity(id);
    }

    public List<Rental> findByCarLicensePlate(String licensePlate){
        if(licensePlate == null){
            logger.warn(String.format("findByCarLicensePlate called with null licensePlate"));
            return Collections.emptyList();
        }
        logger.info(String.format("Trying to find Rental by Car %s", licensePlate));
        return findByPredicate(x -> x.getCar().getLicensePlate().toLowerCase().trim()
            .equals(licensePlate.toLowerCase().trim()))
            .toList();
    }

    public List<Rental> findByCustomerDriverLicense(String driverLicense){
        if(driverLicense == null){
            logger.warn(String.format("findByCustomerDriverLicense called with null driverLicense"));
            return Collections.emptyList();
        }
        logger.info(String.format("Trying to find Rental by Customer %s", driverLicense));
        return findByPredicate(x -> x.getCustomer().driverLicense().toLowerCase().trim()
            .equals(driverLicense.toLowerCase().trim()))
            .toList();
    }

}
