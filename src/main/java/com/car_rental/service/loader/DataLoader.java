package com.car_rental.service.loader;

import com.car_rental.persistence.PersistenceManager;
import com.car_rental.repository.*;
import com.car_rental.service.LoadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DataLoader {

    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final PersistenceManager persistenceManager;
    private final String format;

    public DataLoader(PersistenceManager persistenceManager, String format) {
        this.persistenceManager = persistenceManager;
        this.format = format;
        logger.info("DataLoader initialized with format: {}", format);
    }

    public DataLoader(PersistenceManager persistenceManager) {
        this(persistenceManager, "JSON");
    }

    public <T> int loadEntity(String entityType, Class<T> clazz, GenericRepository<T> repository) {
        int oldSize = repository.size();
        List<T> items = persistenceManager.load(entityType, clazz, format);
        repository.addAll(items);
        int newSize = repository.size();
        int loadedCount = newSize - oldSize;
        logger.info("Loaded {} new items of type {}", loadedCount, entityType);
        return loadedCount;
    }

    public LoadResult load(
            BranchRepository branchRepository,
            CarRepository carRepository,
            CustomerRepository customerRepository,
            RentalRepository rentalRepository,
            LoadingStrategy loadingStrategy
    ) {
        logger.info("Loading data using strategy: {}", loadingStrategy.getClass().getSimpleName());
        return loadingStrategy.load(
                branchRepository,
                carRepository,
                customerRepository,
                rentalRepository,
                this
        );
    }
}
