package com.car_rental.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;
import java.util.Optional;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;
import com.car_rental.model.Customer;
import com.car_rental.model.Payment;
import com.car_rental.model.PaymentMethod;
import com.car_rental.model.Rental;

class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
    }

    private Payment createTestPayment(String id, double amount, String paymentDate, PaymentMethod paymentMethod) {
        Car car = new Car("СЕ0303СХ", "Toyota Camry", 2020, 25000.0, CarStatus.AVAILABLE);
        Customer customer = new Customer("Іван", "Петренко", "АВТ123456", "01.01.1990");
        Rental rental = new Rental("R001", car, customer, "01.01.2024", "07.01.2024");
        return new Payment(id, rental, amount, paymentDate, paymentMethod);
    }

    private Payment createTestPayment(String id, Rental rental, double amount, String paymentDate, PaymentMethod paymentMethod) {
        return new Payment(id, rental, amount, paymentDate, paymentMethod);
    }

    private Rental createTestRental(String id, String carLicense, String driverLicense) {
        Car car = new Car(carLicense, "Toyota Camry", 2020, 25000.0, CarStatus.AVAILABLE);
        Customer customer = new Customer("Іван", "Петренко", driverLicense, "01.01.1990");
        return new Rental(id, car, customer, "01.01.2024", "07.01.2024");
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Constructor should create empty PaymentRepository")
    void testConstructor() {
        assertTrue(paymentRepository.isEmpty());
        assertEquals(0, paymentRepository.size());
    }

    // ========== sortByPaymentDate() Tests ==========

    @Test
    @DisplayName("sortByPaymentDate should return payments sorted by payment date in ascending order")
    void testSortByPaymentDate() {
        paymentRepository.add(createTestPayment("P001", 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));
        paymentRepository.add(createTestPayment("P002", 300.0, "10.03.2024", PaymentMethod.CASH));
        paymentRepository.add(createTestPayment("P003", 750.0, "20.03.2024", PaymentMethod.DEBIT_CARD));

        List<Payment> sortedPayments = paymentRepository.sortByPaymentDate();

        assertEquals(3, sortedPayments.size());
        assertEquals("10.03.2024", sortedPayments.get(0).getPaymentDate().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("15.03.2024", sortedPayments.get(1).getPaymentDate().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("20.03.2024", sortedPayments.get(2).getPaymentDate().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Test
    @DisplayName("sortByPaymentDate should handle empty repository")
    void testSortByPaymentDateEmpty() {
        List<Payment> sortedPayments = paymentRepository.sortByPaymentDate();
        
        assertTrue(sortedPayments.isEmpty());
    }

    @Test
    @DisplayName("sortByPaymentDate should handle single payment")
    void testSortByPaymentDateSinglePayment() {
        paymentRepository.add(createTestPayment("P001", 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));

        List<Payment> sortedPayments = paymentRepository.sortByPaymentDate();

        assertEquals(1, sortedPayments.size());
        assertEquals("15.03.2024", sortedPayments.get(0).getPaymentDate().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Test
    @DisplayName("sortByPaymentDate should handle payments with same date")
    void testSortByPaymentDateSameDate() {
        paymentRepository.add(createTestPayment("P001", 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));
        paymentRepository.add(createTestPayment("P002", 300.0, "15.03.2024", PaymentMethod.CASH));

        List<Payment> sortedPayments = paymentRepository.sortByPaymentDate();

        assertEquals(2, sortedPayments.size());
        assertEquals("15.03.2024", sortedPayments.get(0).getPaymentDate().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("15.03.2024", sortedPayments.get(1).getPaymentDate().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    // ========== sortByAmount() Tests ==========

    @Test
    @DisplayName("sortByAmount should return payments sorted by amount in ascending order")
    void testSortByAmount() {
        paymentRepository.add(createTestPayment("P001", 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));
        paymentRepository.add(createTestPayment("P002", 300.0, "10.03.2024", PaymentMethod.CASH));
        paymentRepository.add(createTestPayment("P003", 750.0, "20.03.2024", PaymentMethod.DEBIT_CARD));

        List<Payment> sortedPayments = paymentRepository.sortByAmount();

        assertEquals(3, sortedPayments.size());
        assertEquals(300.0, sortedPayments.get(0).getAmount());
        assertEquals(500.0, sortedPayments.get(1).getAmount());
        assertEquals(750.0, sortedPayments.get(2).getAmount());
    }

    @Test
    @DisplayName("sortByAmount should handle empty repository")
    void testSortByAmountEmpty() {
        List<Payment> sortedPayments = paymentRepository.sortByAmount();
        
        assertTrue(sortedPayments.isEmpty());
    }

    @Test
    @DisplayName("sortByAmount should handle single payment")
    void testSortByAmountSinglePayment() {
        paymentRepository.add(createTestPayment("P001", 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));

        List<Payment> sortedPayments = paymentRepository.sortByAmount();

        assertEquals(1, sortedPayments.size());
        assertEquals(500.0, sortedPayments.get(0).getAmount());
    }

    @Test
    @DisplayName("sortByAmount should handle payments with same amount")
    void testSortByAmountSameAmount() {
        paymentRepository.add(createTestPayment("P001", 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));
        paymentRepository.add(createTestPayment("P002", 500.0, "10.03.2024", PaymentMethod.CASH));

        List<Payment> sortedPayments = paymentRepository.sortByAmount();

        assertEquals(2, sortedPayments.size());
        assertEquals(500.0, sortedPayments.get(0).getAmount());
        assertEquals(500.0, sortedPayments.get(1).getAmount());
    }

    // ========== findById() Tests ==========

    @Test
    @DisplayName("findById should return payment when ID exists")
    void testFindByIdExists() {
        Payment payment = createTestPayment("P001", 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD);
        paymentRepository.add(payment);

        Optional<Payment> result = paymentRepository.findById("P001");

        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
        assertEquals("P001", result.get().getId());
    }

    @Test
    @DisplayName("findById should return empty when ID does not exist")
    void testFindByIdNotExists() {
        paymentRepository.add(createTestPayment("P001", 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));

        Optional<Payment> result = paymentRepository.findById("P999");

        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findById should return empty when ID is null")
    void testFindByIdNull(String id) {
        paymentRepository.add(createTestPayment("P001", 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));

        Optional<Payment> result = paymentRepository.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findById should return empty when repository is empty")
    void testFindByIdEmptyRepository() {
        Optional<Payment> result = paymentRepository.findById("P001");

        assertFalse(result.isPresent());
    }

    // ========== findByRentalId() Tests ==========

    @Test
    @DisplayName("findByRentalId should return payments matching rental ID")
    void testFindByRentalIdExists() {
        Rental rental1 = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        Rental rental2 = createTestRental("R002", "СЕ0304СХ", "БДЕ234567");
        
        paymentRepository.add(createTestPayment("P001", rental1, 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));
        paymentRepository.add(createTestPayment("P002", rental1, 300.0, "10.03.2024", PaymentMethod.CASH));
        paymentRepository.add(createTestPayment("P003", rental2, 750.0, "20.03.2024", PaymentMethod.DEBIT_CARD));

        List<Payment> result = paymentRepository.findByRentalId("R001");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(payment -> payment.getRental().getId().equals("R001")));
        assertTrue(result.stream().anyMatch(payment -> payment.getId().equals("P001")));
        assertTrue(result.stream().anyMatch(payment -> payment.getId().equals("P002")));
    }

    @Test
    @DisplayName("findByRentalId should return empty list when rental ID does not exist")
    void testFindByRentalIdNotExists() {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        paymentRepository.add(createTestPayment("P001", rental, 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));

        List<Payment> result = paymentRepository.findByRentalId("R999");

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("findByRentalId should return empty list when rental ID is null")
    void testFindByRentalIdNull(String rentalId) {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        paymentRepository.add(createTestPayment("P001", rental, 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));

        List<Payment> result = paymentRepository.findByRentalId(rentalId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByRentalId should return empty list when repository is empty")
    void testFindByRentalIdEmptyRepository() {
        List<Payment> result = paymentRepository.findByRentalId("R001");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByRentalId should handle case insensitive search")
    void testFindByRentalIdCaseInsensitive() {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        paymentRepository.add(createTestPayment("P001", rental, 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));

        List<Payment> resultLower = paymentRepository.findByRentalId("r001");
        List<Payment> resultUpper = paymentRepository.findByRentalId("R001");

        assertEquals(1, resultLower.size());
        assertEquals(1, resultUpper.size());
        assertEquals("P001", resultLower.get(0).getId());
        assertEquals("P001", resultUpper.get(0).getId());
    }

    @Test
    @DisplayName("findByRentalId should handle whitespace in rental ID")
    void testFindByRentalIdWithWhitespace() {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        paymentRepository.add(createTestPayment("P001", rental, 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));

        List<Payment> result = paymentRepository.findByRentalId("  R001  ");

        assertEquals(1, result.size());
        assertEquals("P001", result.get(0).getId());
    }

    @Test
    @DisplayName("findByRentalId should return single matching payment")
    void testFindByRentalIdSingleMatch() {
        Rental rental1 = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        Rental rental2 = createTestRental("R002", "СЕ0304СХ", "БДЕ234567");
        
        paymentRepository.add(createTestPayment("P001", rental1, 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));
        paymentRepository.add(createTestPayment("P002", rental2, 300.0, "10.03.2024", PaymentMethod.CASH));

        List<Payment> result = paymentRepository.findByRentalId("R001");

        assertEquals(1, result.size());
        assertEquals("P001", result.get(0).getId());
        assertEquals("R001", result.get(0).getRental().getId());
    }

    // ========== Integration Tests ==========

    @Test
    @DisplayName("All methods should work together correctly")
    void testIntegrationAllMethods() {
        // Create test rentals
        Rental rental1 = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        Rental rental2 = createTestRental("R002", "СЕ0304СХ", "БДЕ234567");

        // Add multiple payments
        paymentRepository.add(createTestPayment("P001", rental1, 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));
        paymentRepository.add(createTestPayment("P002", rental1, 300.0, "10.03.2024", PaymentMethod.CASH));
        paymentRepository.add(createTestPayment("P003", rental2, 750.0, "20.03.2024", PaymentMethod.DEBIT_CARD));
        paymentRepository.add(createTestPayment("P004", rental2, 200.0, "05.03.2024", PaymentMethod.ONLINE));

        // Test sorting by payment date
        List<Payment> byDate = paymentRepository.sortByPaymentDate();
        assertEquals("05.03.2024", byDate.get(0).getPaymentDate().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals("20.03.2024", byDate.get(3).getPaymentDate().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        // Test sorting by amount
        List<Payment> byAmount = paymentRepository.sortByAmount();
        assertEquals(200.0, byAmount.get(0).getAmount());
        assertEquals(750.0, byAmount.get(3).getAmount());

        // Test finding by ID
        Optional<Payment> foundPayment = paymentRepository.findById("P002");
        assertTrue(foundPayment.isPresent());
        assertEquals(300.0, foundPayment.get().getAmount());

        // Test finding by rental ID
        List<Payment> rental1Payments = paymentRepository.findByRentalId("R001");
        assertEquals(2, rental1Payments.size());
        assertTrue(rental1Payments.stream().allMatch(payment -> payment.getRental().getId().equals("R001")));

        List<Payment> rental2Payments = paymentRepository.findByRentalId("R002");
        assertEquals(2, rental2Payments.size());
        assertTrue(rental2Payments.stream().allMatch(payment -> payment.getRental().getId().equals("R002")));
    }

    // ========== Edge Cases ==========

    @Test
    @DisplayName("Repository should handle payments with different payment methods")
    void testDifferentPaymentMethods() {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        
        paymentRepository.add(createTestPayment("P001", rental, 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD));
        paymentRepository.add(createTestPayment("P002", rental, 300.0, "10.03.2024", PaymentMethod.CASH));
        paymentRepository.add(createTestPayment("P003", rental, 750.0, "20.03.2024", PaymentMethod.DEBIT_CARD));
        paymentRepository.add(createTestPayment("P004", rental, 200.0, "05.03.2024", PaymentMethod.ONLINE));

        List<Payment> allPayments = paymentRepository.getAll();
        assertEquals(4, allPayments.size());
        
        // Verify different payment methods are preserved
        assertTrue(allPayments.stream().anyMatch(p -> p.getPaymentMethod() == PaymentMethod.CREDIT_CARD));
        assertTrue(allPayments.stream().anyMatch(p -> p.getPaymentMethod() == PaymentMethod.CASH));
        assertTrue(allPayments.stream().anyMatch(p -> p.getPaymentMethod() == PaymentMethod.DEBIT_CARD));
        assertTrue(allPayments.stream().anyMatch(p -> p.getPaymentMethod() == PaymentMethod.ONLINE));
    }

    @Test
    @DisplayName("Repository should maintain data integrity across operations")
    void testDataIntegrity() {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        Payment payment = createTestPayment("P001", rental, 500.0, "15.03.2024", PaymentMethod.CREDIT_CARD);
        paymentRepository.add(payment);

        // Original payment should not be modified by repository operations
        List<Payment> sortedByDate = paymentRepository.sortByPaymentDate();
        List<Payment> sortedByAmount = paymentRepository.sortByAmount();
        Optional<Payment> found = paymentRepository.findById("P001");
        List<Payment> byRentalId = paymentRepository.findByRentalId("R001");

        // Verify all operations return the same data
        assertEquals("P001", payment.getId());
        assertEquals("P001", sortedByDate.get(0).getId());
        assertEquals("P001", sortedByAmount.get(0).getId());
        assertEquals("P001", found.get().getId());
        assertEquals("P001", byRentalId.get(0).getId());
        
        // Verify amounts are consistent
        assertEquals(500.0, payment.getAmount());
        assertEquals(500.0, sortedByDate.get(0).getAmount());
        assertEquals(500.0, sortedByAmount.get(0).getAmount());
        assertEquals(500.0, found.get().getAmount());
        assertEquals(500.0, byRentalId.get(0).getAmount());
    }

    @Test
    @DisplayName("Repository should handle decimal amounts correctly")
    void testDecimalAmounts() {
        Rental rental = createTestRental("R001", "СЕ0303СХ", "АВТ123456");
        
        paymentRepository.add(createTestPayment("P001", rental, 123.45, "15.03.2024", PaymentMethod.CREDIT_CARD));
        paymentRepository.add(createTestPayment("P002", rental, 67.89, "10.03.2024", PaymentMethod.CASH));

        List<Payment> sortedByAmount = paymentRepository.sortByAmount();
        
        assertEquals(67.89, sortedByAmount.get(0).getAmount(), 0.01);
        assertEquals(123.45, sortedByAmount.get(1).getAmount(), 0.01);
    }
}