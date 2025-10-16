package com.car_rental.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.model.Customer;

public class CustomerRepository  extends GenericRepository<Customer>{
    private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);


    public CustomerRepository(){
        super(Customer::driverLicense, "Customer");
    }

    public List<Customer> sortByFirstName(){
        return sortByComparator(Customer.byFirstName());
    }

    public List<Customer> sortByLastName(){
        return sortByComparator(Customer.byLastName());
    }

    public List<Customer> sortByBirthDate(){
        return sortByComparator(Customer.byBirthDate());
    }

    public Optional<Customer> findByDriverLicense(String driverLicence){
        if(driverLicence == null){
            logger.warn(String.format("findByDriverLicense called with null driverLicence"));
            return Optional.empty();
        }
        logger.info(String.format("Trying to find Customer by %s", driverLicence));
        return findByIdentity(driverLicence);
    }

    public List<Customer> findByBirthDate(String birthDate){
        if(birthDate == null){
            logger.warn(String.format("findByBirthDate called with null birthDate"));
            return Collections.emptyList();
        }
        logger.info(String.format("Trying to find Customer by %s", birthDate));
        return findByPredicate(x -> x.birthDate().equals(birthDate))
            .toList();
    }

    
}
