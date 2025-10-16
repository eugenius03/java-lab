package com.car_rental.repository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;
import com.car_rental.model.Customer;
import com.car_rental.model.Rental;

class RentalRepositoryTest {

    private RentalRepository rentalRepository;

    @BeforeEach
    void setUp() {
        rentalRepository = new RentalRepository();
    }

    private Rental createTestRental(String id, String carLicense, String customerDriverLicense, String startDate, String endDate) {
        Car car = new Car(carLicense, "Toyota Camry", 2020, 25000.0, CarStatus.AVAILABLE);
        Customer customer = new Customer("Іван", "Петренко", customerDriverLicense, "01.01.1990");
        return new Rental(id, car, customer, startDate, endDate);
    }

    private Rental createTestRental(String id, String carLicense, String customerDriverLicense) {
        Car car = new Car(carLicense, "Toyota Camry", 2020, 25000.0, CarStatus.AVAILABLE);
        Customer customer = new Customer("Іван", "Петренко", customerDriverLicense, "01.01.1990");
        return new Rental(id, car, customer);
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Constructor should create empty RentalRepository")
    void testConstructor() {
        assertTrue(rentalRepository.isEmpty());
        assertEquals(0, rentalRepository.size());
    }

    // ========== sortByStartDate() Tests ==========

    @Test
    @DisplayName("sortByStartDate should return rentals sorted by start date in ascending order")
    void testSortByStartDate() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));
        rentalRepository.add(createTestRental("R002", "СЕ0304СХ", "БДЕ234567", "10.03.2024", "15.03.2024"));
        rentalRepository.add(createTestRental("R003", "СЕ0305СХ", "ВЖЗ345678", "20.03.2024", "25.03.2024"));

        List<Rental> sortedRentals = rentalRepository.sortByStartDate();

        assertEquals(3, sortedRentals.size());
        assertEquals("10.03.2024", sortedRentals.get(0).getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("15.03.2024", sortedRentals.get(1).getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("20.03.2024", sortedRentals.get(2).getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Test
    @DisplayName("sortByStartDate should handle empty repository")
    void testSortByStartDateEmpty() {
        List<Rental> sortedRentals = rentalRepository.sortByStartDate();
        
        assertTrue(sortedRentals.isEmpty());
    }

    @Test
    @DisplayName("sortByStartDate should handle single rental")
    void testSortByStartDateSingleRental() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> sortedRentals = rentalRepository.sortByStartDate();

        assertEquals(1, sortedRentals.size());
        assertEquals("15.03.2024", sortedRentals.get(0).getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Test
    @DisplayName("sortByStartDate should handle rentals with same start date")
    void testSortByStartDateSameDate() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));
        rentalRepository.add(createTestRental("R002", "СЕ0304СХ", "БДЕ234567", "15.03.2024", "18.03.2024"));

        List<Rental> sortedRentals = rentalRepository.sortByStartDate();

        assertEquals(2, sortedRentals.size());
        assertEquals("15.03.2024", sortedRentals.get(0).getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("15.03.2024", sortedRentals.get(1).getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    // ========== sortByEndDate() Tests ==========

    @Test
    @DisplayName("sortByEndDate should return rentals sorted by end date in ascending order")
    void testSortByEndDate() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "25.03.2024"));
        rentalRepository.add(createTestRental("R002", "СЕ0304СХ", "БДЕ234567", "10.03.2024", "20.03.2024"));
        rentalRepository.add(createTestRental("R003", "СЕ0305СХ", "ВЖЗ345678", "20.03.2024", "30.03.2024"));

        List<Rental> sortedRentals = rentalRepository.sortByEndDate();

        assertEquals(3, sortedRentals.size());
        assertEquals("20.03.2024", sortedRentals.get(0).getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("25.03.2024", sortedRentals.get(1).getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("30.03.2024", sortedRentals.get(2).getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Test
    @DisplayName("sortByEndDate should handle empty repository")
    void testSortByEndDateEmpty() {
        List<Rental> sortedRentals = rentalRepository.sortByEndDate();
        
        assertTrue(sortedRentals.isEmpty());
    }

    @Test
    @DisplayName("sortByEndDate should handle single rental")
    void testSortByEndDateSingleRental() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> sortedRentals = rentalRepository.sortByEndDate();

        assertEquals(1, sortedRentals.size());
        assertEquals("20.03.2024", sortedRentals.get(0).getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Test
    @DisplayName("sortByEndDate should handle rentals with same end date")
    void testSortByEndDateSameDate() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));
        rentalRepository.add(createTestRental("R002", "СЕ0304СХ", "БДЕ234567", "12.03.2024", "20.03.2024"));

        List<Rental> sortedRentals = rentalRepository.sortByEndDate();

        assertEquals(2, sortedRentals.size());
        assertEquals("20.03.2024", sortedRentals.get(0).getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("20.03.2024", sortedRentals.get(1).getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    // ========== findById() Tests ==========

    @Test
    @DisplayName("findById should return rental when ID exists")
    void testFindByIdExists() {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024");
        rentalRepository.add(rental);

        Optional<Rental> result = rentalRepository.findById("R001");

        assertTrue(result.isPresent());
        assertEquals(rental, result.get());
        assertEquals("R001", result.get().getId());
    }

    @Test
    @DisplayName("findById should return empty when ID does not exist")
    void testFindByIdNotExists() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        Optional<Rental> result = rentalRepository.findById("R999");

        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findById should return empty when ID is null")
    void testFindByIdNull(String id) {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        Optional<Rental> result = rentalRepository.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findById should return empty when repository is empty")
    void testFindByIdEmptyRepository() {
        Optional<Rental> result = rentalRepository.findById("R001");

        assertFalse(result.isPresent());
    }

    // ========== findByCarLicensePlate() Tests ==========

    @Test
    @DisplayName("findByCarLicensePlate should return rentals matching car license plate")
    void testFindByCarLicensePlateExists() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));
        rentalRepository.add(createTestRental("R002", "СЕ0303СХ", "БДЕ234567", "10.03.2024", "15.03.2024"));
        rentalRepository.add(createTestRental("R003", "СЕ0304СХ", "ВЖЗ345678", "20.03.2024", "25.03.2024"));

        List<Rental> result = rentalRepository.findByCarLicensePlate("СЕ0303СХ");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(rental -> rental.getCar().getLicensePlate().equals("СЕ0303СХ")));
        assertTrue(result.stream().anyMatch(rental -> rental.getId().equals("R001")));
        assertTrue(result.stream().anyMatch(rental -> rental.getId().equals("R002")));
    }

    @Test
    @DisplayName("findByCarLicensePlate should return empty list when license plate does not exist")
    void testFindByCarLicensePlateNotExists() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> result = rentalRepository.findByCarLicensePlate("СЕ9999СХ");

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findByCarLicensePlate should return empty list when license plate is null")
    void testFindByCarLicensePlateNull(String licensePlate) {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> result = rentalRepository.findByCarLicensePlate(licensePlate);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByCarLicensePlate should return empty list when repository is empty")
    void testFindByCarLicensePlateEmptyRepository() {
        List<Rental> result = rentalRepository.findByCarLicensePlate("СЕ0303СХ");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByCarLicensePlate should handle case insensitive search")
    void testFindByCarLicensePlateCaseInsensitive() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> resultLower = rentalRepository.findByCarLicensePlate("се0303сх");
        List<Rental> resultUpper = rentalRepository.findByCarLicensePlate("СЕ0303СХ");

        assertEquals(1, resultLower.size());
        assertEquals(1, resultUpper.size());
        assertEquals("R001", resultLower.get(0).getId());
        assertEquals("R001", resultUpper.get(0).getId());
    }

    @Test
    @DisplayName("findByCarLicensePlate should handle whitespace in license plate")
    void testFindByCarLicensePlateWithWhitespace() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> result = rentalRepository.findByCarLicensePlate("  СЕ0303СХ  ");

        assertEquals(1, result.size());
        assertEquals("R001", result.get(0).getId());
    }

    @Test
    @DisplayName("findByCarLicensePlate should return single matching rental")
    void testFindByCarLicensePlateSingleMatch() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));
        rentalRepository.add(createTestRental("R002", "СЕ0304СХ", "БДЕ234567", "10.03.2024", "15.03.2024"));

        List<Rental> result = rentalRepository.findByCarLicensePlate("СЕ0303СХ");

        assertEquals(1, result.size());
        assertEquals("R001", result.get(0).getId());
        assertEquals("СЕ0303СХ", result.get(0).getCar().getLicensePlate());
    }

    // ========== findByCustomerDriverLicense() Tests ==========

    @Test
    @DisplayName("findByCustomerDriverLicense should return rentals matching customer driver license")
    void testFindByCustomerDriverLicenseExists() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));
        rentalRepository.add(createTestRental("R002", "СЕ0304СХ", "АВТ123456", "10.03.2024", "15.03.2024"));
        rentalRepository.add(createTestRental("R003", "СЕ0305СХ", "БДЕ234567", "20.03.2024", "25.03.2024"));

        List<Rental> result = rentalRepository.findByCustomerDriverLicense("АВТ123456");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(rental -> rental.getCustomer().driverLicense().equals("АВТ123456")));
        assertTrue(result.stream().anyMatch(rental -> rental.getId().equals("R001")));
        assertTrue(result.stream().anyMatch(rental -> rental.getId().equals("R002")));
    }

    @Test
    @DisplayName("findByCustomerDriverLicense should return empty list when driver license does not exist")
    void testFindByCustomerDriverLicenseNotExists() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> result = rentalRepository.findByCustomerDriverLicense("ЯЧС999999");

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findByCustomerDriverLicense should return empty list when driver license is null")
    void testFindByCustomerDriverLicenseNull(String driverLicense) {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> result = rentalRepository.findByCustomerDriverLicense(driverLicense);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByCustomerDriverLicense should return empty list when repository is empty")
    void testFindByCustomerDriverLicenseEmptyRepository() {
        List<Rental> result = rentalRepository.findByCustomerDriverLicense("АВТ123456");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByCustomerDriverLicense should handle case insensitive search")
    void testFindByCustomerDriverLicenseCaseInsensitive() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> resultLower = rentalRepository.findByCustomerDriverLicense("авт123456");
        List<Rental> resultUpper = rentalRepository.findByCustomerDriverLicense("АВТ123456");

        assertEquals(1, resultLower.size());
        assertEquals(1, resultUpper.size());
        assertEquals("R001", resultLower.get(0).getId());
        assertEquals("R001", resultUpper.get(0).getId());
    }

    @Test
    @DisplayName("findByCustomerDriverLicense should handle whitespace in driver license")
    void testFindByCustomerDriverLicenseWithWhitespace() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));

        List<Rental> result = rentalRepository.findByCustomerDriverLicense("  АВТ123456  ");

        assertEquals(1, result.size());
        assertEquals("R001", result.get(0).getId());
    }

    @Test
    @DisplayName("findByCustomerDriverLicense should return single matching rental")
    void testFindByCustomerDriverLicenseSingleMatch() {
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024"));
        rentalRepository.add(createTestRental("R002", "СЕ0304СХ", "БДЕ234567", "10.03.2024", "15.03.2024"));

        List<Rental> result = rentalRepository.findByCustomerDriverLicense("АВТ123456");

        assertEquals(1, result.size());
        assertEquals("R001", result.get(0).getId());
        assertEquals("АВТ123456", result.get(0).getCustomer().driverLicense());
    }

    // ========== Integration Tests ==========

    @Test
    @DisplayName("All methods should work together correctly")
    void testIntegrationAllMethods() {
        // Add multiple rentals
        rentalRepository.add(createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "25.03.2024"));
        rentalRepository.add(createTestRental("R002", "СЕ0304СХ", "БДЕ234567", "10.03.2024", "20.03.2024"));
        rentalRepository.add(createTestRental("R003", "СЕ0305СХ", "ВЖЗ345678", "20.03.2024", "30.03.2024"));
        rentalRepository.add(createTestRental("R004", "СЕ0303СХ", "АВТ123456", "05.03.2024", "15.03.2024"));

        // Test sorting by start date
        List<Rental> byStartDate = rentalRepository.sortByStartDate();
        assertEquals("05.03.2024", byStartDate.get(0).getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("20.03.2024", byStartDate.get(3).getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        // Test sorting by end date
        List<Rental> byEndDate = rentalRepository.sortByEndDate();
        assertEquals("15.03.2024", byEndDate.get(0).getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("30.03.2024", byEndDate.get(3).getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        // Test finding by ID
        Optional<Rental> foundRental = rentalRepository.findById("R002");
        assertTrue(foundRental.isPresent());
        assertEquals("БДЕ234567", foundRental.get().getCustomer().driverLicense());

        // Test finding by car license plate
        List<Rental> carRentals = rentalRepository.findByCarLicensePlate("СЕ0303СХ");
        assertEquals(2, carRentals.size());
        assertTrue(carRentals.stream().allMatch(rental -> rental.getCar().getLicensePlate().equals("СЕ0303СХ")));

        // Test finding by customer driver license
        List<Rental> customerRentals = rentalRepository.findByCustomerDriverLicense("АВТ123456");
        assertEquals(2, customerRentals.size());
        assertTrue(customerRentals.stream().allMatch(rental -> rental.getCustomer().driverLicense().equals("АВТ123456")));
    }

    // ========== Edge Cases ==========

    @Test
    @DisplayName("Repository should handle rentals with different customers and cars")
    void testDifferentCustomersAndCars() {
        Car car1 = new Car("СЕ0303СХ", "Toyota Camry", 2020, 25000.0, CarStatus.AVAILABLE);
        Car car2 = new Car("СЕ0304СХ", "Honda Civic", 2021, 15000.0, CarStatus.AVAILABLE);
        Customer customer1 = new Customer("Іван", "Петренко", "АВТ123456", "01.01.1990");
        Customer customer2 = new Customer("Оксана", "Іваненко", "БДЕ234567", "02.02.1985");
        
        rentalRepository.add(new Rental("R001", car1, customer1, "15.03.2024", "20.03.2024"));
        rentalRepository.add(new Rental("R002", car2, customer2, "10.03.2024", "15.03.2024"));

        List<Rental> allRentals = rentalRepository.getAll();
        assertEquals(2, allRentals.size());
        
        // Verify different cars and customers are preserved
        assertTrue(allRentals.stream().anyMatch(r -> r.getCar().getLicensePlate().equals("СЕ0303СХ")));
        assertTrue(allRentals.stream().anyMatch(r -> r.getCar().getLicensePlate().equals("СЕ0304СХ")));
        assertTrue(allRentals.stream().anyMatch(r -> r.getCustomer().firstName().equals("Іван")));
        assertTrue(allRentals.stream().anyMatch(r -> r.getCustomer().firstName().equals("Оксана")));
    }

    @Test
    @DisplayName("Repository should maintain data integrity across operations")
    void testDataIntegrity() {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456", "15.03.2024", "20.03.2024");
        rentalRepository.add(rental);

        // Original rental should not be modified by repository operations
        List<Rental> sortedByStartDate = rentalRepository.sortByStartDate();
        List<Rental> sortedByEndDate = rentalRepository.sortByEndDate();
        Optional<Rental> found = rentalRepository.findById("R001");
        List<Rental> byCarLicense = rentalRepository.findByCarLicensePlate("СЕ0303СХ");
        List<Rental> byDriverLicense = rentalRepository.findByCustomerDriverLicense("АВТ123456");

        // Verify all operations return the same data
        assertEquals("R001", rental.getId());
        assertEquals("R001", sortedByStartDate.get(0).getId());
        assertEquals("R001", sortedByEndDate.get(0).getId());
        assertEquals("R001", found.get().getId());
        assertEquals("R001", byCarLicense.get(0).getId());
        assertEquals("R001", byDriverLicense.get(0).getId());
        
        // Verify car license plates are consistent
        assertEquals("СЕ0303СХ", rental.getCar().getLicensePlate());
        assertEquals("СЕ0303СХ", sortedByStartDate.get(0).getCar().getLicensePlate());
        assertEquals("СЕ0303СХ", sortedByEndDate.get(0).getCar().getLicensePlate());
        assertEquals("СЕ0303СХ", found.get().getCar().getLicensePlate());
        assertEquals("СЕ0303СХ", byCarLicense.get(0).getCar().getLicensePlate());
        assertEquals("СЕ0303СХ", byDriverLicense.get(0).getCar().getLicensePlate());
    }

    @Test
    @DisplayName("Repository should handle rentals with default dates (constructor without dates)")
    void testDefaultDates() {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        rentalRepository.add(rental);

        List<Rental> allRentals = rentalRepository.getAll();
        assertEquals(1, allRentals.size());
        
        Rental retrievedRental = allRentals.get(0);
        assertNotNull(retrievedRental.getStartDate());
        assertNotNull(retrievedRental.getEndDate());
        assertTrue(retrievedRental.getEndDate().isAfter(retrievedRental.getStartDate()));
    }
}