package com.car_rental.service.loader;

import com.car_rental.repository.BranchRepository;
import com.car_rental.repository.CarRepository;
import com.car_rental.repository.CustomerRepository;
import com.car_rental.repository.RentalRepository;
import com.car_rental.service.LoadResult;

@FunctionalInterface
public interface LoadingStrategy {

    LoadResult load(
            BranchRepository branchRepository,
            CarRepository carRepository,
            CustomerRepository customerRepository,
            RentalRepository rentalRepository,
            DataLoader dataLoader
    );
}
