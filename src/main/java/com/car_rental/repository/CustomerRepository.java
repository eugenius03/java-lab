package com.car_rental.repository;

import com.car_rental.model.Customer;

public class CustomerRepository{

    private final GenericRepository<Customer> repository;

    public CustomerRepository(){
        repository = new GenericRepository<>(Customer::driverLicense, "Customer");
    }

    
}
