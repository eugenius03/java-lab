package com.car_rental.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.slf4j.LoggerFactory;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

class CarRepositoryTest {

    private GenericRepository<Car> carRepository;
    private ListAppender<ILoggingEvent> logAppender;
    private IdentityExtractor<Car> licensePlateExtractor;

    @BeforeEach
    void setUp() {

        Logger logger = (Logger) LoggerFactory.getLogger(GenericRepository.class);
        logAppender = new ListAppender<>();
        logAppender.start();
        logger.addAppender(logAppender);
        
        licensePlateExtractor = Car::getLicensePlate;
        
        carRepository = new GenericRepository<>(licensePlateExtractor, "Car");
    }

    private Car createTestCar(String licensePlate, String model) {
        Car car = new Car();
        car.setLicensePlate(licensePlate);
        car.setModel(model);
        car.setYear(2020);
        car.setMileage(50000.0);
        car.setStatus(CarStatus.AVAILABLE);
        return car;
    }

    @Test
    @DisplayName("Constructor should create empty repository")
    void testConstructor() {
        assertTrue(carRepository.isEmpty());
        assertEquals(0, carRepository.size());
    }

    @Test
    @DisplayName("Add valid car should succeed")
    void testAddValidCar() {
        Car car = createTestCar("СЕ0303СХ", "Toyota Camry");
        
        boolean result = carRepository.add(car);
        
        assertTrue(result);
        assertEquals(1, carRepository.size());
        assertTrue(carRepository.contains(car));
        
    }

    @Test
    @DisplayName("Add null car should fail")
    void testAddNullCar() {
        boolean result = carRepository.add(null);
        
        assertFalse(result);
        assertEquals(0, carRepository.size());
    }

    @Test
    @DisplayName("Add duplicate car should fail")
    void testAddDuplicateCar() {
        Car car1 = createTestCar("СЕ0303СХ", "Toyota Camry");
        Car car2 = createTestCar("СЕ0303СХ", "Honda Civic");
        
        carRepository.add(car1);
        boolean result = carRepository.add(car2);
        
        assertFalse(result);
        assertEquals(1, carRepository.size());
        
    }

    @Test
    @DisplayName("AddAll with null should fail")
    void testAddAllNull() {
        boolean result = carRepository.addAll(null);
        
        assertFalse(result);
        assertEquals(0, carRepository.size());
    }

    @Test
    @DisplayName("AddAll with valid list should process all items")
    void testAddAllValidList() {
        List<Car> cars = Arrays.asList(
            createTestCar("СЕ0303СХ", "Toyota Camry"),
            createTestCar("СЕ0304СХ", "Honda Civic")
        );
        
        boolean result = carRepository.addAll(cars);
        
        assertTrue(result);
        assertEquals(cars.size(), carRepository.size());
    }

    @Test
    @DisplayName("AddAll with mixed new and existing items should add only new ones")
    void testAddAllMixedItems() {
        List<Car> cars = Arrays.asList(
            createTestCar("СЕ0303СХ", "Toyota Camry"),
            createTestCar("СЕ0303СХ", "Honda Civic")
        );
        
        boolean result = carRepository.addAll(cars);
        
        assertTrue(result);
        assertEquals("Toyota Camry", carRepository.findByIdentity("СЕ0303СХ").get().getModel());
        assertEquals(1, carRepository.size());
    }

    @Test
    @DisplayName("AddAll with all existing items should return false")
    void testAddAllAllExisting() {
        Car car1 = createTestCar("СЕ0303СХ", "Toyota Camry");
        Car car2 = createTestCar("СЕ0304СХ", "Honda Civic");
        
        // Pre-add both cars
        carRepository.add(car1);
        carRepository.add(car2);
        
        List<Car> duplicateCars = Arrays.asList(
            createTestCar("СЕ0303СХ", "Different Model"),
            createTestCar("СЕ0304СХ", "Another Model")
        );
        
        boolean result = carRepository.addAll(duplicateCars);
        
        assertFalse(result);
        assertEquals(2, carRepository.size());
        
    }

    @Test
    @DisplayName("Get by index should return correct car")
    void testGet() {
        Car car = createTestCar("СЕ0303СХ", "Toyota Camry");
        carRepository.add(car);
        
        Car retrieved = carRepository.get(0);
        
        assertEquals(car, retrieved);
    }

    @Test
    @DisplayName("Get with invalid index should throw exception")
    void testGetInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> carRepository.get(0));
    }

    @Test
    @DisplayName("Remove existing car should succeed")
    void testRemoveExistingCar() {
        Car car = createTestCar("СЕ0303СХ", "Toyota Camry");
        carRepository.add(car);
        
        boolean result = carRepository.remove(car);
        
        assertTrue(result);
        assertEquals(0, carRepository.size());
        assertFalse(carRepository.contains(car));
        
    }

    @Test
    @DisplayName("Remove null car should fail")
    void testRemoveNull() {
        boolean result = carRepository.remove(null);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Remove non-existing car should fail")
    void testRemoveNonExisting() {
        Car car = createTestCar("СЕ0303СХ", "Toyota Camry");
        
        boolean result = carRepository.remove(car);
        
        assertFalse(result);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Remove by null license plate should fail")
    void testRemoveByLicensePlateNull(String licensePlate) {
        boolean result = carRepository.removeByIdentity(licensePlate);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Remove by non-existing license plate should fail")
    void testRemoveByLicensePlateNonExisting() {
        boolean result = carRepository.removeByIdentity("NON_EXISTING");
        
        assertFalse(result);
    }

    @Test
    @DisplayName("Contains should work correctly")
    void testContains() {
        Car car = createTestCar("СЕ0303СХ", "Toyota Camry");
        
        assertFalse(carRepository.contains(car));
        
        carRepository.add(car);
        assertTrue(carRepository.contains(car));
    }

    @Test
    @DisplayName("ContainsIdentity should work correctly")
    void testContainsIdentity() {
        Car car = createTestCar("СЕ0303СХ", "Toyota Camry");
        
        assertFalse(carRepository.containsIdentity("СЕ0303СХ"));
        
        carRepository.add(car);
        assertTrue(carRepository.containsIdentity("СЕ0303СХ"));
        assertFalse(carRepository.containsIdentity("XYZ789"));
    }

    @Test
    @DisplayName("FindByIdentity should return correct car")
    void testFindByLicensePlateExists() {
        Car car = createTestCar("СЕ0303СХ", "Toyota Camry");
        carRepository.add(car);
        
        Optional<Car> result = carRepository.findByIdentity("СЕ0303СХ");
        
        assertTrue(result.isPresent());
        assertEquals(car, result.get());
    }

    @Test
    @DisplayName("FindByIdentity should return empty for non-existing license plate")
    void testFindByLicensePlateNotExists() {
        Optional<Car> result = carRepository.findByIdentity("NON_EXISTING");
        
        assertFalse(result.isPresent());        
    }

    @Test
    @DisplayName("FindByIdentity with null should return empty")
    void testFindByLicensePlateNull() {
        Optional<Car> result = carRepository.findByIdentity(null);
        
        assertFalse(result.isPresent());
        
    }

    @Test
    @DisplayName("Size and isEmpty should work correctly")
    void testSizeAndIsEmpty() {
        assertTrue(carRepository.isEmpty());
        assertEquals(0, carRepository.size());
        
        carRepository.add(createTestCar("СЕ0303СХ", "Toyota Camry"));
        assertFalse(carRepository.isEmpty());
        assertEquals(1, carRepository.size());
        
        carRepository.add(createTestCar("СЕ0304СХ", "Honda Civic"));
        assertEquals(2, carRepository.size());
    }

    @Test
    @DisplayName("Clear should remove all cars")
    void testClear() {
        carRepository.add(createTestCar("СЕ0303СХ", "Toyota Camry"));
        carRepository.add(createTestCar("СЕ0304СХ", "Honda Civic"));
        
        carRepository.clear();
        
        assertTrue(carRepository.isEmpty());
        assertEquals(0, carRepository.size());
        
    }
}