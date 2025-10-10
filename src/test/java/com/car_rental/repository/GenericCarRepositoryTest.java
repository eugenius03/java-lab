package com.car_rental.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;

class GenericCarRepositoryTest {

    private GenericRepository<Car> carRepository;
    private IdentityExtractor<Car> licensePlateExtractor;

    @BeforeEach
    void setUp() {
        
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
    // ========== sortByDefault() Tests ==========
    
    @Test
    @DisplayName("sortByDefault should sort cars by status priority (natural ordering)")
    void testSortByDefaultValidCars() {
        carRepository.add(new Car("СЕ0303СХ", "Toyota", 2020, 50000, CarStatus.RENTED));
        carRepository.add(new Car("СЕ0304СХ", "Honda 123", 2021, 30000, CarStatus.AVAILABLE));
        carRepository.add(new Car("СЕ0305СХ", "Ford Mustang", 2019, 80000, CarStatus.MAINTENANCE));
        carRepository.add(new Car("СЕ0306СХ", "BMW X7", 2022, 10000, CarStatus.RESERVED));
        
        List<Car> sorted = carRepository.sortByDefault();
        
        assertEquals(4, sorted.size());
        // Should be sorted by status priority: AVAILABLE(1), RESERVED(2), RENTED(3), MAINTENANCE(4)
        assertEquals(CarStatus.AVAILABLE, sorted.get(0).getStatus());
        assertEquals(CarStatus.RESERVED, sorted.get(1).getStatus());
        assertEquals(CarStatus.RENTED, sorted.get(2).getStatus());
        assertEquals(CarStatus.MAINTENANCE, sorted.get(3).getStatus());
        
    }

    @Test
    @DisplayName("sortByDefault should handle empty repository")
    void testSortByDefaultEmptyRepository() {
        List<Car> sorted = carRepository.sortByDefault();
        
        assertTrue(sorted.isEmpty());        
    }

    @Test
    @DisplayName("sortByDefault should not modify original repository")
    void testSortByDefaultDoesNotModifyOriginal() {
        Car car1 = new Car("СЕ0303СХ", "Honda", 2021, 30000, CarStatus.RENTED);
        Car car2 = new Car("СЕ0304СХ", "Toyota", 2020, 50000, CarStatus.AVAILABLE);
        
        carRepository.add(car1);
        carRepository.add(car2);
        
        List<Car> sorted = carRepository.sortByDefault();
        List<Car> original = carRepository.getAll();
        
        assertEquals(CarStatus.AVAILABLE, sorted.get(0).getStatus());
        assertEquals(CarStatus.RENTED, sorted.get(1).getStatus());
        
        assertEquals("СЕ0303СХ", original.get(0).getLicensePlate());
        assertEquals("СЕ0304СХ", original.get(1).getLicensePlate());
    }

    // ========== sortByComparator() Tests ==========
    
    @Test
    @DisplayName("sortByComparator should sort using provided comparator")
    void testSortByComparatorValid() {
        carRepository.add(new Car("СЕ0303СХ", "Honda", 2021, 30000, CarStatus.AVAILABLE));
        carRepository.add(new Car("СЕ0304СХ", "Toyota", 2020, 50000, CarStatus.AVAILABLE));
        carRepository.add(new Car("СЕ0305СХ", "Ford Mustang", 2022, 10000, CarStatus.AVAILABLE));
        
        List<Car> sortedByYear = carRepository.sortByComparator(Car.byYear().reversed());
        
        assertEquals(3, sortedByYear.size());
        assertEquals(2022, sortedByYear.get(0).getYear());
        assertEquals(2021, sortedByYear.get(1).getYear());
        assertEquals(2020, sortedByYear.get(2).getYear());
    }

    @Test
    @DisplayName("sortByComparator should handle null comparator")
    void testSortByComparatorNull() {
        carRepository.add(new Car("СЕ0303СХ", "Toyota", 2020, 50000, CarStatus.AVAILABLE));
        carRepository.add(new Car("СЕ0304СХ", "Honda", 2021, 30000, CarStatus.AVAILABLE));
        
        List<Car> result = carRepository.sortByComparator(null);
        
        assertEquals(2, result.size());
        assertEquals("СЕ0303СХ", result.get(0).getLicensePlate());
        assertEquals("СЕ0304СХ", result.get(1).getLicensePlate());    
        
        List<Car> byMileage = carRepository.sortByComparator(Car.byMileage());

        assertEquals(30000, byMileage.get(0).getMileage(), 0.001);
        assertEquals(50000, byMileage.get(1).getMileage(), 0.001);
    }

    @Test
    @DisplayName("sortByComparator should handle empty repository")
    void testSortByComparatorEmpty() {
        List<Car> sorted = carRepository.sortByComparator(Car.byYear());
        
        assertTrue(sorted.isEmpty());    
    }
    // ========== sortByIdentity() Tests ==========

    @ParameterizedTest
    @ValueSource(strings = {"asc", "ASC", "ascending", "ASCENDING", "Asc", "Ascending"})
    @DisplayName("sortByIdentity should handle various ascending formats")
    void testSortByIdentityAscendingVariations(String order) {
        carRepository.add(new Car("СЕ0303СХ", "BMW X7", 2022, 5000, CarStatus.AVAILABLE));
        carRepository.add(new Car("СЕ0304СХ", "Audi RS6", 2020, 80000, CarStatus.AVAILABLE));
        
        List<Car> sorted = carRepository.sortByIdentity(order);
        
        assertEquals("СЕ0303СХ", sorted.get(0).getLicensePlate());
        assertEquals("СЕ0304СХ", sorted.get(1).getLicensePlate());
    }

    @ParameterizedTest
    @ValueSource(strings = {"desc", "DESC", "descending", "DESCENDING", "Desc", "Descending"})
    @DisplayName("sortByIdentity should handle various descending formats")
    void testSortByIdentityDescendingVariations(String order) {
        carRepository.add(new Car("СЕ0303СХ", "BMW X7", 2022, 5000, CarStatus.AVAILABLE));
        carRepository.add(new Car("СЕ0304СХ", "Audi RS6", 2020, 80000, CarStatus.AVAILABLE));
        
        List<Car> sorted = carRepository.sortByIdentity(order);
        
        assertEquals("СЕ0304СХ", sorted.get(0).getLicensePlate());
        assertEquals("СЕ0303СХ", sorted.get(1).getLicensePlate());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("sortByIdentity should handle null order parameter")
    void testSortByIdentityNullOrder(String order) {
        carRepository.add(new Car("СЕ0303СХ", "Toyota", 2020, 50000, CarStatus.AVAILABLE));
        
        List<Car> result = carRepository.sortByIdentity(order);
        
        assertEquals(1, result.size());
        assertEquals("СЕ0303СХ", result.get(0).getLicensePlate());
    }

    @Test
    @DisplayName("sortByIdentity should handle empty repository")
    void testSortByIdentityEmpty() {
        List<Car> sorted = carRepository.sortByIdentity("asc");
        
        assertTrue(sorted.isEmpty());
    }

}