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

import com.car_rental.model.Customer;

class CustomerRepositoryTest {

    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
    }

    private Customer createTestCustomer(String firstName, String lastName, String driverLicense, String birthDate) {
        return new Customer(firstName, lastName, driverLicense, birthDate);
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Constructor should create empty CustomerRepository")
    void testConstructor() {
        assertTrue(customerRepository.isEmpty());
        assertEquals(0, customerRepository.size());
    }

    // ========== sortByFirstName() Tests ==========

    @Test
    @DisplayName("sortByFirstName should return customers sorted by first name in ascending order")
    void testSortByFirstName() {
        customerRepository.add(createTestCustomer("Василь", "Петренко", "АВТ123456", "01.01.1990"));
        customerRepository.add(createTestCustomer("Анна", "Іваненко", "БДЕ234567", "02.02.1985"));
        customerRepository.add(createTestCustomer("Олексій", "Коваленко", "ВЖЗ345678", "03.03.1992"));

        List<Customer> sortedCustomers = customerRepository.sortByFirstName();

        assertEquals(3, sortedCustomers.size());
        assertEquals("Анна", sortedCustomers.get(0).firstName());
        assertEquals("Василь", sortedCustomers.get(1).firstName());
        assertEquals("Олексій", sortedCustomers.get(2).firstName());
    }

    @Test
    @DisplayName("sortByFirstName should handle empty repository")
    void testSortByFirstNameEmpty() {
        List<Customer> sortedCustomers = customerRepository.sortByFirstName();
        
        assertTrue(sortedCustomers.isEmpty());
    }

    @Test
    @DisplayName("sortByFirstName should handle single customer")
    void testSortByFirstNameSingleCustomer() {
        customerRepository.add(createTestCustomer("Анна", "Іваненко", "АВТ123456", "01.01.1990"));

        List<Customer> sortedCustomers = customerRepository.sortByFirstName();

        assertEquals(1, sortedCustomers.size());
        assertEquals("Анна", sortedCustomers.get(0).firstName());
    }

    @Test
    @DisplayName("sortByFirstName should handle customers with same first name")
    void testSortByFirstNameSameName() {
        customerRepository.add(createTestCustomer("Анна", "Петренко", "АВТ123456", "01.01.1990"));
        customerRepository.add(createTestCustomer("Анна", "Іваненко", "БДЕ234567", "02.02.1985"));

        List<Customer> sortedCustomers = customerRepository.sortByFirstName();

        assertEquals(2, sortedCustomers.size());
        assertEquals("Анна", sortedCustomers.get(0).firstName());
        assertEquals("Анна", sortedCustomers.get(1).firstName());
    }

    // ========== sortByLastName() Tests ==========

    @Test
    @DisplayName("sortByLastName should return customers sorted by last name in ascending order")
    void testSortByLastName() {
        customerRepository.add(createTestCustomer("Іван", "Шевченко", "АВТ123456", "01.01.1990"));
        customerRepository.add(createTestCustomer("Оксана", "Петренко", "БДЕ234567", "02.02.1985"));
        customerRepository.add(createTestCustomer("Олег", "Коваленко", "ВЖЗ345678", "03.03.1992"));

        List<Customer> sortedCustomers = customerRepository.sortByLastName();

        assertEquals(3, sortedCustomers.size());
        assertEquals("Коваленко", sortedCustomers.get(0).lastName());
        assertEquals("Петренко", sortedCustomers.get(1).lastName());
        assertEquals("Шевченко", sortedCustomers.get(2).lastName());
    }

    @Test
    @DisplayName("sortByLastName should handle empty repository")
    void testSortByLastNameEmpty() {
        List<Customer> sortedCustomers = customerRepository.sortByLastName();
        
        assertTrue(sortedCustomers.isEmpty());
    }

    @Test
    @DisplayName("sortByLastName should handle single customer")
    void testSortByLastNameSingleCustomer() {
        customerRepository.add(createTestCustomer("Анна", "Іваненко", "АВТ123456", "01.01.1990"));

        List<Customer> sortedCustomers = customerRepository.sortByLastName();

        assertEquals(1, sortedCustomers.size());
        assertEquals("Іваненко", sortedCustomers.get(0).lastName());
    }

    @Test
    @DisplayName("sortByLastName should handle customers with same last name")
    void testSortByLastNameSameName() {
        customerRepository.add(createTestCustomer("Анна", "Іваненко", "АВТ123456", "01.01.1990"));
        customerRepository.add(createTestCustomer("Олег", "Іваненко", "БДЕ234567", "02.02.1985"));

        List<Customer> sortedCustomers = customerRepository.sortByLastName();

        assertEquals(2, sortedCustomers.size());
        assertEquals("Іваненко", sortedCustomers.get(0).lastName());
        assertEquals("Іваненко", sortedCustomers.get(1).lastName());
    }

    // ========== sortByBirthDate() Tests ==========

    @Test
    @DisplayName("sortByBirthDate should return customers sorted by birth date in ascending order")
    void testSortByBirthDate() {
        customerRepository.add(createTestCustomer("Іван", "Шевченко", "АВТ123456", "01.01.1995"));
        customerRepository.add(createTestCustomer("Оксана", "Петренко", "БДЕ234567", "01.01.1985"));
        customerRepository.add(createTestCustomer("Олег", "Коваленко", "ВЖЗ345678", "01.01.1990"));

        List<Customer> sortedCustomers = customerRepository.sortByBirthDate();

        assertEquals(3, sortedCustomers.size());
        assertEquals("01.01.1985", sortedCustomers.get(0).birthDate());
        assertEquals("01.01.1990", sortedCustomers.get(1).birthDate());
        assertEquals("01.01.1995", sortedCustomers.get(2).birthDate());
    }

    @Test
    @DisplayName("sortByBirthDate should handle empty repository")
    void testSortByBirthDateEmpty() {
        List<Customer> sortedCustomers = customerRepository.sortByBirthDate();
        
        assertTrue(sortedCustomers.isEmpty());
    }

    @Test
    @DisplayName("sortByBirthDate should handle single customer")
    void testSortByBirthDateSingleCustomer() {
        customerRepository.add(createTestCustomer("Анна", "Іваненко", "АВТ123456", "01.01.1990"));

        List<Customer> sortedCustomers = customerRepository.sortByBirthDate();

        assertEquals(1, sortedCustomers.size());
        assertEquals("01.01.1990", sortedCustomers.get(0).birthDate());
    }

    @Test
    @DisplayName("sortByBirthDate should handle customers with same birth date")
    void testSortByBirthDateSameDate() {
        customerRepository.add(createTestCustomer("Анна", "Іваненко", "АВТ123456", "01.01.1990"));
        customerRepository.add(createTestCustomer("Олег", "Коваленко", "БДЕ234567", "01.01.1990"));

        List<Customer> sortedCustomers = customerRepository.sortByBirthDate();

        assertEquals(2, sortedCustomers.size());
        assertEquals("01.01.1990", sortedCustomers.get(0).birthDate());
        assertEquals("01.01.1990", sortedCustomers.get(1).birthDate());
    }

    // ========== findByDriverLicense() Tests ==========

    @Test
    @DisplayName("findByDriverLicense should return customer when driver license exists")
    void testFindByDriverLicenseExists() {
        Customer customer = createTestCustomer("Анна", "Іваненко", "АВТ123456", "01.01.1990");
        customerRepository.add(customer);

        Optional<Customer> result = customerRepository.findByDriverLicense("АВТ123456");

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
        assertEquals("АВТ123456", result.get().driverLicense());
    }

    @Test
    @DisplayName("findByDriverLicense should return empty when driver license does not exist")
    void testFindByDriverLicenseNotExists() {
        customerRepository.add(createTestCustomer("Анна", "Іваненко", "АВТ123456", "01.01.1990"));

        Optional<Customer> result = customerRepository.findByDriverLicense("ЯЧС999999");

        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findByDriverLicense should return empty when driver license is null")
    void testFindByDriverLicenseNull(String driverLicense) {
        customerRepository.add(createTestCustomer("Анна", "Іваненко", "АВТ123456", "01.01.1990"));

        Optional<Customer> result = customerRepository.findByDriverLicense(driverLicense);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findByDriverLicense should return empty when repository is empty")
    void testFindByDriverLicenseEmptyRepository() {
        Optional<Customer> result = customerRepository.findByDriverLicense("АВТ123456");

        assertFalse(result.isPresent());
    }

    // ========== findByBirthDate() Tests ==========

    @Test
    @DisplayName("findByBirthDate should return customers with matching birth date")
    void testFindByBirthDateExists() {
        customerRepository.add(createTestCustomer("Іван", "Шевченко", "АВТ123456", "01.01.1990"));
        customerRepository.add(createTestCustomer("Оксана", "Петренко", "БДЕ234567", "01.01.1985"));
        customerRepository.add(createTestCustomer("Олег", "Коваленко", "ВЖЗ345678", "01.01.1990"));

        List<Customer> result = customerRepository.findByBirthDate("01.01.1990");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(customer -> customer.birthDate().equals("01.01.1990")));
        assertTrue(result.stream().anyMatch(customer -> customer.firstName().equals("Іван")));
        assertTrue(result.stream().anyMatch(customer -> customer.firstName().equals("Олег")));
    }

    @Test
    @DisplayName("findByBirthDate should return empty list when birth date does not exist")
    void testFindByBirthDateNotExists() {
        customerRepository.add(createTestCustomer("Іван", "Шевченко", "АВТ123456", "01.01.1990"));

        List<Customer> result = customerRepository.findByBirthDate("31.12.1999");

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findByBirthDate should return empty list when birth date is null")
    void testFindByBirthDateNull(String birthDate) {
        customerRepository.add(createTestCustomer("Іван", "Шевченко", "АВТ123456", "01.01.1990"));

        List<Customer> result = customerRepository.findByBirthDate(birthDate);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByBirthDate should return empty list when repository is empty")
    void testFindByBirthDateEmptyRepository() {
        List<Customer> result = customerRepository.findByBirthDate("01.01.1990");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByBirthDate should handle single matching customer")
    void testFindByBirthDateSingleMatch() {
        customerRepository.add(createTestCustomer("Іван", "Шевченко", "АВТ123456", "01.01.1990"));
        customerRepository.add(createTestCustomer("Оксана", "Петренко", "БДЕ234567", "01.01.1985"));

        List<Customer> result = customerRepository.findByBirthDate("01.01.1990");

        assertEquals(1, result.size());
        assertEquals("Іван", result.get(0).firstName());
        assertEquals("01.01.1990", result.get(0).birthDate());
    }

    @Test
    @DisplayName("findByBirthDate should be case sensitive and exact match")
    void testFindByBirthDateExactMatch() {
        customerRepository.add(createTestCustomer("Іван", "Шевченко", "АВТ123456", "01.01.1990"));

        List<Customer> result = customerRepository.findByBirthDate("1.1.1990");

        assertTrue(result.isEmpty());
    }

    // ========== Integration Tests ==========

    @Test
    @DisplayName("All methods should work together correctly")
    void testIntegrationAllMethods() {
        // Add multiple customers
        customerRepository.add(createTestCustomer("Іван", "Шевченко", "АВТ123456", "01.01.1995"));
        customerRepository.add(createTestCustomer("Оксана", "Петренко", "БДЕ234567", "10.03.1985"));
        customerRepository.add(createTestCustomer("Олег", "Коваленко", "ВЖЗ345678", "01.01.1990"));
        customerRepository.add(createTestCustomer("Ігор", "Василенко", "АБГ492393", "10.03.1985"));

        // Test sorting by first name
        List<Customer> byFirstName = customerRepository.sortByFirstName();
        assertEquals("Іван", byFirstName.get(0).firstName());
        assertEquals("Олег", byFirstName.get(3).firstName());

        // Test sorting by last name
        List<Customer> byLastName = customerRepository.sortByLastName();
        assertEquals("Василенко", byLastName.get(0).lastName());
        assertEquals("Шевченко", byLastName.get(3).lastName());

        // Test sorting by birth date
        List<Customer> byBirthDate = customerRepository.sortByBirthDate();
        assertEquals("10.03.1985", byBirthDate.get(0).birthDate());
        assertEquals("01.01.1995", byBirthDate.get(3).birthDate());

        // Test finding by driver license
        Optional<Customer> foundCustomer = customerRepository.findByDriverLicense("АВТ123456");
        assertTrue(foundCustomer.isPresent());
        assertEquals("Іван", foundCustomer.get().firstName());

        // Test finding by birth date
        List<Customer> sameBirthDate = customerRepository.findByBirthDate("10.03.1985");
        assertEquals(2, sameBirthDate.size());
        assertTrue(sameBirthDate.stream().anyMatch(customer -> customer.firstName().equals("Оксана")));
        assertTrue(sameBirthDate.stream().anyMatch(customer -> customer.firstName().equals("Ігор")));
    }
}