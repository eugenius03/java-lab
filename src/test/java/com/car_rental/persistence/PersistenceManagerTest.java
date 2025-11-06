package com.car_rental.persistence;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.car_rental.config.AppConfig;
import com.car_rental.model.Branch;
import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;
import com.car_rental.model.Customer;

class PersistenceManagerTest {
    
    private PersistenceManager persistenceManager;
    private Path tempDataDir;
    
    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempDataDir = tempDir;
        // Mock AppConfig to use temp directory
        AppConfig config = new AppConfig() {
            // Override methods if needed to point to tempDir
        };
        persistenceManager = new PersistenceManager(config);
    }
    
    @Test
    void testSaveAndLoadCarsJson() {
        // Arrange
        List<Car> cars = Arrays.asList(
            new Car("СЕ1234ЕК", "Toyota Corolla", 2019, 30000, CarStatus.AVAILABLE),
            new Car("АА5678КС", "Ford Focus", 2016, 55000, CarStatus.AVAILABLE)
        );
        
        // Act
        persistenceManager.save(cars, "test_cars", Car.class, "JSON");
        List<Car> loaded = persistenceManager.load("test_cars", Car.class, "JSON");
        
        // Assert
        assertNotNull(loaded);
        assertEquals(2, loaded.size());
        assertEquals("СЕ1234ЕК", loaded.get(0).getLicensePlate());
        assertEquals("Ford Focus", loaded.get(1).getModel());
    }
    
    @Test
    void testSaveAndLoadCarsYaml() {
        // Arrange
        List<Car> cars = Arrays.asList(
            new Car("АТ9012МК", "Hyundai i30", 2021, 15000, CarStatus.MAINTENANCE)
        );
        
        // Act
        persistenceManager.save(cars, "test_cars_yaml", Car.class, "YAML");
        List<Car> loaded = persistenceManager.load("test_cars_yaml", Car.class, "YAML");
        
        // Assert
        assertNotNull(loaded);
        assertEquals(1, loaded.size());
        assertEquals("АТ9012МК", loaded.get(0).getLicensePlate());
        assertEquals(CarStatus.MAINTENANCE, loaded.get(0).getStatus());
    }
    
    @Test
    void testSaveAndLoadCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(
            new Customer("Olena", "Shevchenko", "ХЕН123456", "12.05.1992"),
            new Customer("Nikita", "Kovalenko", "ХНВ123456", "03.11.1988")
        );
        
        // Act
        persistenceManager.save(customers, "test_customers", Customer.class, "JSON");
        List<Customer> loaded = persistenceManager.load("test_customers", Customer.class, "JSON");
        
        // Assert
        assertNotNull(loaded);
        assertEquals(2, loaded.size());
        assertEquals("Olena", loaded.get(0).firstName());
        assertEquals("ХНВ123456", loaded.get(1).driverLicense());
    }
    
    @Test
    void testSaveAndLoadBranches() {
        // Arrange
        List<Branch> branches = Arrays.asList(
            new Branch("Main Office", "Kyiv"),
            new Branch("Branch 1", "Lviv")
        );
        
        // Act
        persistenceManager.save(branches, "test_branches", Branch.class, "JSON");
        List<Branch> loaded = persistenceManager.load("test_branches", Branch.class, "JSON");
        
        // Assert
        assertNotNull(loaded);
        assertEquals(2, loaded.size());
        assertEquals("Main Office", loaded.get(0).name());
        assertEquals("Lviv", loaded.get(1).location());
    }
    
    @Test
    void testSaveAllFormats() {
        // Arrange
        List<Car> cars = Arrays.asList(
            new Car("EE9012FF", "Ford Mustang", 2019, 60000, CarStatus.AVAILABLE)
        );
        
        // Act
        persistenceManager.saveAllFormats(cars, "test_all_formats", Car.class);
        
        List<Car> jsonLoaded = persistenceManager.load("test_all_formats", Car.class, "JSON");
        List<Car> yamlLoaded = persistenceManager.load("test_all_formats", Car.class, "YAML");
        
        // Assert
        assertNotNull(jsonLoaded);
        assertNotNull(yamlLoaded);
        assertEquals(1, jsonLoaded.size());
        assertEquals(1, yamlLoaded.size());
        assertEquals("EE9012FF", jsonLoaded.get(0).getLicensePlate());
        assertEquals("EE9012FF", yamlLoaded.get(0).getLicensePlate());
    }
    
    @Test
    void testLoadNonExistentFile() {
        // Act
        assertThrows(Exception.class, () -> 
            persistenceManager.load("nonexistent_file.json", Car.class, "JSON")
        );
    }
    
    @Test
    void testSaveEmptyList() {
        // Arrange
        List<Car> emptyCars = List.of();
        
        // Act
        persistenceManager.save(emptyCars, "empty_cars", Car.class, "JSON");
        List<Car> loaded = persistenceManager.load("empty_cars", Car.class, "JSON");
        
        // Assert
        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }
    
    @Test
    void testOverwriteExistingFile() {
        // Arrange
        List<Car> initialCars = Arrays.asList(
            new Car("AA1111AA", "Old Car", 2010, 100000, CarStatus.MAINTENANCE)
        );
        List<Car> updatedCars = Arrays.asList(
            new Car("BB2222BB", "New Car", 2023, 5000, CarStatus.AVAILABLE)
        );
        
        // Act
        persistenceManager.save(initialCars, "overwrite_test", Car.class, "JSON");
        persistenceManager.save(updatedCars, "overwrite_test", Car.class, "JSON");
        List<Car> loaded = persistenceManager.load("overwrite_test", Car.class, "JSON");
        
        // Assert
        assertNotNull(loaded);
        assertEquals(1, loaded.size());
        assertEquals("BB2222BB", loaded.get(0).getLicensePlate());
        assertEquals("New Car", loaded.get(0).getModel());
    }
}