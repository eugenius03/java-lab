package com.car_rental;

import com.car_rental.config.AppConfig;
import com.car_rental.persistence.PersistenceManager;
import com.car_rental.repository.BranchRepository;
import com.car_rental.repository.CarRepository;
import com.car_rental.repository.CustomerRepository;
import com.car_rental.repository.RentalRepository;
import com.car_rental.service.LoadResult;
import com.car_rental.service.loader.DataLoader;
import com.car_rental.service.loader.ExecutorLoadingStrategy;
import com.car_rental.service.loader.ParallelLoadingStrategy;
import com.car_rental.service.loader.SequentialLoadingStrategy;

public class Main {
    public static void main(String[] args) {

        AppConfig appConfig = new AppConfig();
        PersistenceManager persistenceManager = new PersistenceManager(appConfig);

        BranchRepository branchRepository = new BranchRepository();
        CarRepository carRepository = new CarRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        RentalRepository rentalRepository = new RentalRepository();

        demonstrateDataLoading(persistenceManager, branchRepository, carRepository, customerRepository, rentalRepository);

    }

    private static void demonstrateDataLoading(
            PersistenceManager persistenceManager,
            BranchRepository branchRepository,
            CarRepository carRepository,
            CustomerRepository customerRepository,
            RentalRepository rentalRepository
    ) {
        DataLoader dataLoader = new DataLoader(persistenceManager);

        LoadResult sequentialResult = dataLoader.load(
                branchRepository,
                carRepository,
                customerRepository,
                rentalRepository,
                new SequentialLoadingStrategy()
        );
        System.out.println("Sequential Loading Result: " + sequentialResult);

        clearRepositories(branchRepository, carRepository, customerRepository, rentalRepository);

        LoadResult parallelResult = dataLoader.load(
                branchRepository,
                carRepository,
                customerRepository,
                rentalRepository,
                new ParallelLoadingStrategy()
        );

        System.out.println("Parallel Loading Result: " + parallelResult);

        clearRepositories(branchRepository, carRepository, customerRepository, rentalRepository);

        LoadResult executorResult = dataLoader.load(
                branchRepository,
                carRepository,
                customerRepository,
                rentalRepository,
                new ExecutorLoadingStrategy(4)
        );

        System.out.println("Executor Loading Result: " + executorResult);
        System.out.println("\n=== Loading Time Comparison ===");
        System.out.printf("Sequential:      %d ms%n", sequentialResult.durationMs());
        System.out.printf("Parallel:        %d ms%n", parallelResult.durationMs());
        System.out.printf("ExecutorService: %d ms%n", executorResult.durationMs());
    }

    private static void clearRepositories(
            BranchRepository branchRepository,
            CarRepository carRepository,
            CustomerRepository customerRepository,
            RentalRepository rentalRepository
    ) {
        branchRepository.clear();
        carRepository.clear();
        customerRepository.clear();
        rentalRepository.clear();
    }
}
