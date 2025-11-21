package com.car_rental.service.loader;

import com.car_rental.config.AppConfig;
import com.car_rental.model.Branch;
import com.car_rental.model.Car;
import com.car_rental.model.Customer;
import com.car_rental.model.Rental;
import com.car_rental.persistence.PersistenceManager;
import com.car_rental.repository.BranchRepository;
import com.car_rental.repository.CarRepository;
import com.car_rental.repository.CustomerRepository;
import com.car_rental.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataLoaderTest {

    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        AppConfig config = new AppConfig();
        PersistenceManager persistenceManager = new PersistenceManager(config);
        dataLoader = new DataLoader(persistenceManager, "JSON");
    }

    @Test
    void loadEntity_Cars_ShouldLoadEntitiesIntoRepository() {
        CarRepository repo = new CarRepository();

        int loaded = dataLoader.loadEntity("cars", Car.class, repo);

        assertTrue(loaded > 0);
        assertEquals(loaded, repo.size());
    }

    @Test
    void loadEntity_Customers_ShouldLoadEntitiesIntoRepository() {
        CustomerRepository repo = new CustomerRepository();

        int loaded = dataLoader.loadEntity("customers", Customer.class, repo);

        assertTrue(loaded > 0);
        assertEquals(loaded, repo.size());
    }

    @Test
    void loadEntity_Branches_ShouldLoadEntitiesIntoRepository() {
        BranchRepository repo = new BranchRepository();

        int loaded = dataLoader.loadEntity("branches", Branch.class, repo);

        assertTrue(loaded > 0);
        assertEquals(loaded, repo.size());
    }

    @Test
    void loadEntity_Rentals_ShouldLoadEntitiesIntoRepository() {
        RentalRepository repo = new RentalRepository();

        int loaded = dataLoader.loadEntity("rentals", Rental.class, repo);

        assertTrue(loaded > 0);
        assertEquals(loaded, repo.size());
    }

    @Test
    void loadEntity_IntoNonEmptyRepository_ShouldAddNewItems() {
        CarRepository repo = new CarRepository();
        Car testCar = new Car("XX9999XX", "Test Car", 2024, 1.0);
        repo.add(testCar);

        int loaded = dataLoader.loadEntity("cars", Car.class, repo);

        assertEquals(loaded + 1, repo.size());
        assertTrue(repo.findByIdentity("XX9999XX").isPresent());
    }

    @Test
    void loadEntity_WithDuplicates_ShouldSkipExisting() {
        CarRepository repo = new CarRepository();

        int firstLoad = dataLoader.loadEntity("cars", Car.class, repo);
        int firstSize = repo.size();

        int secondLoad = dataLoader.loadEntity("cars", Car.class, repo);

        assertEquals(0, secondLoad, "Second load should add 0 new items (all duplicates)");
        assertEquals(firstSize, repo.size());
    }

    @Test
    void loadEntity_EmptyRepository_ShouldReturnCorrectCount() {
        CarRepository repo = new CarRepository();

        int loaded = dataLoader.loadEntity("cars", Car.class, repo);

        assertEquals(loaded, repo.size());
    }

    @Test
    void loadEntity_MultipleTypes_ShouldWorkIndependently() {
        CarRepository carRepo = new CarRepository();
        CustomerRepository customerRepo = new CustomerRepository();

        int carsLoaded = dataLoader.loadEntity("cars", Car.class, carRepo);
        int customersLoaded = dataLoader.loadEntity("customers", Customer.class, customerRepo);

        assertTrue(carsLoaded > 0);
        assertTrue(customersLoaded > 0);
        assertEquals(carsLoaded, carRepo.size());
        assertEquals(customersLoaded, customerRepo.size());
    }

    @Test
    void loadEntity_Branches_ShouldLoadValidData() {
        BranchRepository repo = new BranchRepository();

        int loaded = dataLoader.loadEntity("branches", Branch.class, repo);

        assertTrue(loaded > 0);
        repo.getAll().forEach(branch -> {
            assertNotNull(branch.name());
            assertNotNull(branch.location());
            assertFalse(branch.name().isEmpty());
            assertFalse(branch.location().isEmpty());
        });
    }

    @Test
    void loadEntity_Cars_ShouldLoadValidData() {
        CarRepository repo = new CarRepository();

        int loaded = dataLoader.loadEntity("cars", Car.class, repo);

        assertTrue(loaded > 0);
        repo.getAll().forEach(car -> {
            assertNotNull(car.getLicensePlate());
            assertNotNull(car.getModel());
            assertTrue(car.getYear() > 0);
            assertTrue(car.getMileage() >= 0);
            assertNotNull(car.getStatus());
        });
    }

    @Test
    void loadEntity_Customers_ShouldLoadValidData() {
        CustomerRepository repo = new CustomerRepository();

        int loaded = dataLoader.loadEntity("customers", Customer.class, repo);

        assertTrue(loaded > 0);
        repo.getAll().forEach(customer -> {
            assertNotNull(customer.firstName());
            assertNotNull(customer.lastName());
            assertNotNull(customer.driverLicense());
            assertNotNull(customer.birthDate());
        });
    }

    @Test
    void loadEntity_WithPartialDuplicates_ShouldAddOnlyNewItems() {
        CarRepository repo = new CarRepository();

        int firstLoad = dataLoader.loadEntity("cars", Car.class, repo);

        Car newCar = new Car("YY8888YY", "New Test Car", 2024, 100);
        repo.add(newCar);
        int sizeAfterManualAdd = repo.size();

        int secondLoad = dataLoader.loadEntity("cars", Car.class, repo);

        assertEquals(0, secondLoad);
        assertEquals(sizeAfterManualAdd, repo.size());
        assertTrue(repo.findByIdentity("YY8888YY").isPresent());
    }

    @Test
    void constructor_WithFormat_ShouldInitialize() {
        AppConfig config = new AppConfig();
        PersistenceManager persistenceManager = new PersistenceManager(config);

        DataLoader loader = new DataLoader(persistenceManager, "YAML");

        assertNotNull(loader);
    }

    @Test
    void constructor_WithDefaultFormat_ShouldInitialize() {
        AppConfig config = new AppConfig();
        PersistenceManager persistenceManager = new PersistenceManager(config);

        DataLoader loader = new DataLoader(persistenceManager);

        assertNotNull(loader);
    }

    @Test
    void load_WithSequentialStrategy_ShouldLoadAllData() {
        BranchRepository branchRepo = new BranchRepository();
        CarRepository carRepo = new CarRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        RentalRepository rentalRepo = new RentalRepository();

        LoadingStrategy strategy = new SequentialLoadingStrategy();
        var result = dataLoader.load(branchRepo, carRepo, customerRepo, rentalRepo, strategy);

        assertNotNull(result);
        assertTrue(result.totalLoaded() > 0);
        assertFalse(branchRepo.isEmpty());
        assertFalse(carRepo.isEmpty());
        assertFalse(customerRepo.isEmpty());
        assertFalse(rentalRepo.isEmpty());
    }

    @Test
    void load_WithParallelStrategy_ShouldLoadAllData() {
        BranchRepository branchRepo = new BranchRepository();
        CarRepository carRepo = new CarRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        RentalRepository rentalRepo = new RentalRepository();

        LoadingStrategy strategy = new ParallelLoadingStrategy();
        var result = dataLoader.load(branchRepo, carRepo, customerRepo, rentalRepo, strategy);

        assertNotNull(result);
        assertTrue(result.totalLoaded() > 0);
        assertFalse(branchRepo.isEmpty());
        assertFalse(carRepo.isEmpty());
        assertFalse(customerRepo.isEmpty());
        assertFalse(rentalRepo.isEmpty());
    }

    @Test
    void load_WithExecutorStrategy_ShouldLoadAllData() {
        BranchRepository branchRepo = new BranchRepository();
        CarRepository carRepo = new CarRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        RentalRepository rentalRepo = new RentalRepository();

        LoadingStrategy strategy = new ExecutorLoadingStrategy();
        var result = dataLoader.load(branchRepo, carRepo, customerRepo, rentalRepo, strategy);

        assertNotNull(result);
        assertTrue(result.totalLoaded() > 0);
        assertFalse(branchRepo.isEmpty());
        assertFalse(carRepo.isEmpty());
        assertFalse(customerRepo.isEmpty());
        assertFalse(rentalRepo.isEmpty());
    }

    @Test
    void loadEntity_ReturnValue_ShouldMatchActualLoadedCount() {
        CarRepository repo = new CarRepository();
        int initialSize = repo.size();

        int returnedCount = dataLoader.loadEntity("cars", Car.class, repo);
        int finalSize = repo.size();

        assertEquals(returnedCount, finalSize - initialSize);
    }
}

