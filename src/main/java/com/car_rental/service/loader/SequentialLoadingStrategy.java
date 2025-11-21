package com.car_rental.service.loader;

import com.car_rental.exception.DataSerializationException;
import com.car_rental.model.Branch;
import com.car_rental.model.Car;
import com.car_rental.model.Customer;
import com.car_rental.model.Rental;
import com.car_rental.repository.BranchRepository;
import com.car_rental.repository.CarRepository;
import com.car_rental.repository.CustomerRepository;
import com.car_rental.repository.RentalRepository;
import com.car_rental.service.LoadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequentialLoadingStrategy implements LoadingStrategy {

    private final Logger logger = LoggerFactory.getLogger(SequentialLoadingStrategy.class);

    @Override
    public LoadResult load(
            BranchRepository branchRepository,
            CarRepository carRepository,
            CustomerRepository customerRepository,
            RentalRepository rentalRepository,
            DataLoader dataLoader
    ) {
        logger.info("Loading data with SequentialLoadingStrategy");
        long startTime = System.currentTimeMillis();

        try {
            int branchesLoaded = dataLoader.loadEntity("branches", Branch.class, branchRepository);
            int carsLoaded = dataLoader.loadEntity("cars", Car.class, carRepository);
            int customersLoaded = dataLoader.loadEntity("customers", Customer.class, customerRepository);
            int rentalsLoaded = dataLoader.loadEntity("rentals", Rental.class, rentalRepository);

            long durationMs = System.currentTimeMillis() - startTime;
            logger.info("Sequential loading done in {} ms", durationMs);

            return new LoadResult(branchesLoaded, carsLoaded, customersLoaded, rentalsLoaded, durationMs);
        } catch (DataSerializationException e) {
            long durationMs = System.currentTimeMillis() - startTime;
            logger.info("Sequential loading failed after {} ms", durationMs);
            throw new RuntimeException("Failed to load data sequentially", e);
        }
    }
}
