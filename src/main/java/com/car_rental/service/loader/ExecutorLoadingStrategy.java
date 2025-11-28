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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorLoadingStrategy implements LoadingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorLoadingStrategy.class);

    private final int threadPoolSize;

    public ExecutorLoadingStrategy(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public ExecutorLoadingStrategy() {
        this(4);
    }

    @Override
    public LoadResult load(
            BranchRepository branchRepository,
            CarRepository carRepository,
            CustomerRepository customerRepository,
            RentalRepository rentalRepository,
            DataLoader dataLoader
    ) {
        logger.info("Loading data with ExecutorLoadingStrategy using thread pool size: {}", threadPoolSize);
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        try {

            CompletableFuture<Integer> branchesFuture = CompletableFuture.supplyAsync(() ->
                    loadEntity(dataLoader, "branches", Branch.class, branchRepository), executorService);

            CompletableFuture<Integer> carsFuture = CompletableFuture.supplyAsync(() ->
                    loadEntity(dataLoader, "cars", Car.class, carRepository), executorService);

            CompletableFuture<Integer> customersFuture = CompletableFuture.supplyAsync(() ->
                    loadEntity(dataLoader, "customers", Customer.class, customerRepository), executorService);

            CompletableFuture<Integer> rentalsFuture = CompletableFuture.supplyAsync(() ->
                    loadEntity(dataLoader, "rentals", Rental.class, rentalRepository), executorService);

            CompletableFuture.allOf(branchesFuture, carsFuture, customersFuture, rentalsFuture).join();

            long durationMs = System.currentTimeMillis() - startTime;
            logger.info("ExecutorService done in {} ms", durationMs);

            return new LoadResult(
                    branchesFuture.join(),
                    carsFuture.join(),
                    customersFuture.join(),
                    rentalsFuture.join(),
                    durationMs
            );
        } finally {
            shutdownExecutor(executorService);
        }

    }

    private <T> int loadEntity(DataLoader dataLoader, String entityType, Class<T> clazz, GenericRepository<T> repository) {
        try {
            return dataLoader.loadEntity(entityType, clazz, repository);
        } catch (DataSerializationException e) {
            throw new RuntimeException(e);
        }
    }

    private void shutdownExecutor(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
