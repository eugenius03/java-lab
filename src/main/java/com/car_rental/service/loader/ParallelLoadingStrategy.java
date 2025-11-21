package com.car_rental.service.loader;

import com.car_rental.exception.DataSerializationException;
import com.car_rental.model.Branch;
import com.car_rental.model.Car;
import com.car_rental.model.Customer;
import com.car_rental.model.Rental;
import com.car_rental.repository.*;
import com.car_rental.service.LoadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ParallelLoadingStrategy implements LoadingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ParallelLoadingStrategy.class);

    @Override
    public LoadResult load(
            BranchRepository branchRepository,
            CarRepository carRepository,
            CustomerRepository customerRepository,
            RentalRepository rentalRepository,
            DataLoader dataLoader
    ) {
        logger.info("Loading data with ParallelLoadingStrategy");
        long startTime = System.currentTimeMillis();

        CompletableFuture<Integer> branchesFuture = CompletableFuture.supplyAsync(() ->
                        loadEntity(dataLoader, "branches", Branch.class, branchRepository))
                .exceptionally(ex -> handleError("branches", ex));

        CompletableFuture<Integer> carsFuture = CompletableFuture.supplyAsync(() ->
                        loadEntity(dataLoader, "cars", Car.class, carRepository))
                .exceptionally(ex -> handleError("cars", ex));

        CompletableFuture<Integer> customersFuture = CompletableFuture.supplyAsync(() ->
                        loadEntity(dataLoader, "customers", Customer.class, customerRepository))
                .exceptionally(ex -> handleError("customers", ex));

        CompletableFuture<Integer> rentalsFuture = CompletableFuture.supplyAsync(() ->
                        loadEntity(dataLoader, "rentals", Rental.class, rentalRepository))
                .exceptionally(ex -> handleError("rentals", ex));

        long durationMs = System.currentTimeMillis() - startTime;
        logger.info("Parallel loading done in {} ms", durationMs);

        return new LoadResult(branchesFuture.join(), carsFuture.join(),
                customersFuture.join(), rentalsFuture.join(), durationMs);
    }

    private <T> int loadEntity(DataLoader dataLoader, String entityType, Class<T> clazz, GenericRepository<T> repository) {
        try {
            return dataLoader.loadEntity(entityType, clazz, repository);
        } catch (DataSerializationException e) {
            throw new RuntimeException(e);
        }
    }

    private int handleError(String entityType, Throwable ex) {
        logger.error("Failed to load {}: {}", entityType, ex.getMessage());
        return 0;
    }
}
