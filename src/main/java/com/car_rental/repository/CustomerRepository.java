package com.car_rental.repository;

import java.util.List;

import com.car_rental.model.Customer;

public class CustomerRepository  extends GenericRepository<Customer>{

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

    
}
