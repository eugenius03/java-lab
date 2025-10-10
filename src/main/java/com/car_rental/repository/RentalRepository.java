package com.car_rental.repository;

import java.util.List;

import com.car_rental.model.Rental;

public class RentalRepository  extends GenericRepository<Rental>{

    public RentalRepository(){
        super(rental -> String.valueOf(rental.getId()), "Rental");
    }

    public List<Rental> sortByStartDate(){
        return sortByComparator(Rental.byStartDate());
    }

    public List<Rental> sortByEndDate(){
        return sortByComparator(Rental.byEndDate());
    }
}
