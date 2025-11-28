package com.car_rental.service;



import java.time.format.DateTimeFormatter;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;
import com.car_rental.model.Customer;
import com.car_rental.model.Payment;
import com.car_rental.model.PaymentMethod;
import com.car_rental.model.Rental;

public class ReportGenerator {

    public static String formatCarStatus(CarStatus status) {
        return switch (status) {
            case AVAILABLE -> "Available";
            case RESERVED -> "Reserved";
            case RENTED -> "Rented";
            case MAINTENANCE -> "Under maintenance";
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }

    public static String formatPaymentMethod(PaymentMethod method) {
        return switch (method) {
            case CASH -> "Cash";
            case CREDIT_CARD -> "Credit card";
            case DEBIT_CARD -> "Debit card";
            case ONLINE -> "Online payment";
            default -> throw new IllegalStateException("Unexpected value: " + method);
        };
    }

    public static String generateCarReport(Car car) {
        return String.format("Car %s (%s), Year: %d, Mileage: %.0f km, Status: %s",
                car.getModel(),
                car.getLicensePlate(),
                car.getYear(),
                car.getMileage(),
                formatCarStatus(car.getStatus()));
    }

    public static String generateRentalReport(Rental rental) {
        Customer c = rental.getCustomer();
        Car car = rental.getCar();
        return String.format("Rental: %s %s rented %s [%s] since %s to %s",
                c.firstName(),
                c.lastName(),
                car.getModel(),
                car.getLicensePlate(),
                rental.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                rental.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    public static String generatePaymentReport(Payment payment) {
        return String.format("Payment: %.2f UAH, Method: %s, Date: %s",
                payment.getAmount(),
                formatPaymentMethod(payment.getPaymentMethod()),
                payment.getPaymentDate());
    }

}
