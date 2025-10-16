package com.car_rental.repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;

class CarRepositoryTest {

    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    private Car createTestCar(String licensePlate, String model, int year, double mileage, CarStatus status) {
        return new Car(licensePlate, model, year, mileage, status);
    }

    private Car createTestCar(String licensePlate, String model, int year, double mileage) {
        return new Car(licensePlate, model, year, mileage);
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Constructor should create empty CarRepository")
    void testConstructor() {
        assertTrue(carRepository.isEmpty());
        assertEquals(0, carRepository.size());
    }

    // ========== sortByYear() Tests ==========

    @Test
    @DisplayName("sortByYear should return cars sorted by year in ascending order")
    void testSortByYear() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0, CarStatus.AVAILABLE));
        carRepository.add(createTestCar("CE0302CX", "Toyota Camry", 2020, 25000.0, CarStatus.AVAILABLE));
        carRepository.add(createTestCar("CE0303CX", "BMW X3", 2021, 20000.0, CarStatus.AVAILABLE));

        List<Car> sortedCars = carRepository.sortByYear();

        assertEquals(3, sortedCars.size());
        assertEquals(2020, sortedCars.get(0).getYear());
        assertEquals(2021, sortedCars.get(1).getYear());
        assertEquals(2022, sortedCars.get(2).getYear());
    }

    @Test
    @DisplayName("sortByYear should handle empty repository")
    void testSortByYearEmpty() {
        List<Car> sortedCars = carRepository.sortByYear();
        
        assertTrue(sortedCars.isEmpty());
    }

    @Test
    @DisplayName("sortByYear should handle single car")
    void testSortByYearSingleCar() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));

        List<Car> sortedCars = carRepository.sortByYear();

        assertEquals(1, sortedCars.size());
        assertEquals(2022, sortedCars.get(0).getYear());
    }

    @Test
    @DisplayName("sortByYear should handle cars with same year")
    void testSortByYearSameYear() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));
        carRepository.add(createTestCar("CE0302CX", "Toyota Camry", 2022, 25000.0));

        List<Car> sortedCars = carRepository.sortByYear();

        assertEquals(2, sortedCars.size());
        assertEquals(2022, sortedCars.get(0).getYear());
        assertEquals(2022, sortedCars.get(1).getYear());
    }

    // ========== sortByMileage() Tests ==========

    @Test
    @DisplayName("sortByMileage should return cars sorted by mileage in ascending order")
    void testSortByMileage() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 30000.0, CarStatus.AVAILABLE));
        carRepository.add(createTestCar("CE0302CX", "Toyota Camry", 2020, 15000.0, CarStatus.AVAILABLE));
        carRepository.add(createTestCar("CE0303CX", "BMW X3", 2021, 45000.0, CarStatus.AVAILABLE));

        List<Car> sortedCars = carRepository.sortByMileage();

        assertEquals(3, sortedCars.size());
        assertEquals(15000.0, sortedCars.get(0).getMileage());
        assertEquals(30000.0, sortedCars.get(1).getMileage());
        assertEquals(45000.0, sortedCars.get(2).getMileage());
    }

    @Test
    @DisplayName("sortByMileage should handle empty repository")
    void testSortByMileageEmpty() {
        List<Car> sortedCars = carRepository.sortByMileage();
        
        assertTrue(sortedCars.isEmpty());
    }

    @Test
    @DisplayName("sortByMileage should handle single car")
    void testSortByMileageSingleCar() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));

        List<Car> sortedCars = carRepository.sortByMileage();

        assertEquals(1, sortedCars.size());
        assertEquals(15000.0, sortedCars.get(0).getMileage());
    }

    @Test
    @DisplayName("sortByMileage should handle cars with same mileage")
    void testSortByMileageSameMileage() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 25000.0));
        carRepository.add(createTestCar("CE0302CX", "Toyota Camry", 2020, 25000.0));

        List<Car> sortedCars = carRepository.sortByMileage();

        assertEquals(2, sortedCars.size());
        assertEquals(25000.0, sortedCars.get(0).getMileage());
        assertEquals(25000.0, sortedCars.get(1).getMileage());
    }

    // ========== findByLicensePlate() Tests ==========

    @Test
    @DisplayName("findByLicensePlate should return car when license plate exists")
    void testFindByLicensePlateExists() {
        Car car = createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0);
        carRepository.add(car);

        Optional<Car> result = carRepository.findByLicensePlate("CE0301CX");

        assertTrue(result.isPresent());
        assertEquals(car, result.get());
        assertEquals("CE0301CX", result.get().getLicensePlate());
    }

    @Test
    @DisplayName("findByLicensePlate should return empty when license plate does not exist")
    void testFindByLicensePlateNotExists() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));

        Optional<Car> result = carRepository.findByLicensePlate("CE0999CX");

        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findByLicensePlate should return empty when license plate is null")
    void testFindByLicensePlateNull(String licensePlate) {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));

        Optional<Car> result = carRepository.findByLicensePlate(licensePlate);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findByLicensePlate should return empty when repository is empty")
    void testFindByLicensePlateEmptyRepository() {
        Optional<Car> result = carRepository.findByLicensePlate("CE0301CX");

        assertFalse(result.isPresent());
    }

    // ========== findByModel() Tests ==========

    @Test
    @DisplayName("findByModel should return cars matching exact model name")
    void testFindByModelExactMatch() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));
        carRepository.add(createTestCar("CE0302CX", "Toyota Camry", 2020, 25000.0));
        carRepository.add(createTestCar("CE0303CX", "Honda Civic", 2021, 20000.0));

        List<Car> result = carRepository.findByModel("Honda Civic");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(car -> car.getModel().equals("Honda Civic")));
    }

    @Test
    @DisplayName("findByModel should return cars matching partial model name (case insensitive)")
    void testFindByModelPartialMatch() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));
        carRepository.add(createTestCar("CE0302CX", "Toyota Camry", 2020, 25000.0));
        carRepository.add(createTestCar("CE0303CX", "Honda Accord", 2021, 20000.0));

        List<Car> result = carRepository.findByModel("honda");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(car -> car.getModel().toLowerCase().contains("honda")));
    }

    @Test
    @DisplayName("findByModel should handle case insensitive search")
    void testFindByModelCaseInsensitive() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));
        carRepository.add(createTestCar("CE0302CX", "TOYOTA CAMRY", 2020, 25000.0));

        List<Car> resultLowerCase = carRepository.findByModel("honda");
        List<Car> resultUpperCase = carRepository.findByModel("HONDA");
        List<Car> resultMixedCase = carRepository.findByModel("Honda");

        assertEquals(1, resultLowerCase.size());
        assertEquals(1, resultUpperCase.size());
        assertEquals(1, resultMixedCase.size());
        assertEquals("Honda Civic", resultLowerCase.get(0).getModel());
    }

    @Test
    @DisplayName("findByModel should handle whitespace in search term")
    void testFindByModelWithWhitespace() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));

        List<Car> result = carRepository.findByModel("  honda  ");

        assertEquals(1, result.size());
        assertEquals("Honda Civic", result.get(0).getModel());
    }

    @Test
    @DisplayName("findByModel should return empty list when model not found")
    void testFindByModelNotFound() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));

        List<Car> result = carRepository.findByModel("BMW");

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findByModel should return empty list when model is null")
    void testFindByModelNull(String model) {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));

        List<Car> result = carRepository.findByModel(model);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByModel should return empty list when repository is empty")
    void testFindByModelEmptyRepository() {
        List<Car> result = carRepository.findByModel("Honda");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByModel should handle empty string")
    void testFindByModelEmptyString() {
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0));

        List<Car> result = carRepository.findByModel("");

        // Should return all cars since empty string matches everything after trim
        assertEquals(1, result.size());
    }

    // ========== Integration Tests ==========

    @Test
    @DisplayName("All methods should work together correctly")
    void testIntegrationAllMethods() {
        // Add multiple cars
        carRepository.add(createTestCar("CE0301CX", "Honda Civic", 2022, 15000.0, CarStatus.AVAILABLE));
        carRepository.add(createTestCar("CE0302CX", "Toyota Camry", 2020, 35000.0, CarStatus.RENTED));
        carRepository.add(createTestCar("CE0303CX", "BMW X3", 2021, 25000.0, CarStatus.AVAILABLE));
        carRepository.add(createTestCar("CE0304CX", "Honda Accord", 2019, 45000.0, CarStatus.MAINTENANCE));

        // Test sorting by year
        List<Car> byYear = carRepository.sortByYear();
        assertEquals(2019, byYear.get(0).getYear());
        assertEquals(2022, byYear.get(3).getYear());

        // Test sorting by mileage
        List<Car> byMileage = carRepository.sortByMileage();
        assertEquals(15000.0, byMileage.get(0).getMileage());
        assertEquals(45000.0, byMileage.get(3).getMileage());

        // Test finding by license plate
        Optional<Car> foundCar = carRepository.findByLicensePlate("CE0302CX");
        assertTrue(foundCar.isPresent());
        assertEquals("Toyota Camry", foundCar.get().getModel());

        // Test finding by model
        List<Car> hondaCars = carRepository.findByModel("Honda");
        assertEquals(2, hondaCars.size());
        assertTrue(hondaCars.stream().allMatch(car -> car.getModel().contains("Honda")));
    }
}