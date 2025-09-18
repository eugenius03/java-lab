package com.car_rental;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;
import com.car_rental.model.Customer;
import com.car_rental.model.Payment;
import com.car_rental.model.PaymentMethod;
import com.car_rental.model.Rental;
import com.car_rental.service.ReportGenerator;

public class Main {
    public static void main(String[] args) {
        Car[] cars = {
            new Car("AB1234CD", "Toyota Corolla", 2019, 30000, CarStatus.AVAILABLE),
            new Car("EF5678GH", "Ford Focus",   2016, 55000, CarStatus.AVAILABLE),
            new Car("IJ9012KL", "Hyundai i30",  2021, 15000, CarStatus.MAINTENANCE)
        };
        
        Customer cust1 = new Customer("Olena", "Shevchenko", "ХЕН123456", "12.05.1992");
        Customer cust2 = new Customer("Nikita", "Kovalenko", "ХНВ123456", "03.11.1988");

        Rental rent1 = new Rental(cars[0], cust1, "01.09.2025", "05.09.2025");
        Rental rent2 = new Rental(cars[1], cust2); // default endDate = now+10 days

        Payment pay1 = new Payment(rent1, 1200.0, "01.09.2025", PaymentMethod.CASH);
        Payment pay2 = new Payment(rent2, 800.0, PaymentMethod.CREDIT_CARD);

        for(Car car : cars) {
            System.out.println(ReportGenerator.generateCarReport(car));
        }
        System.out.println();

        System.out.println(ReportGenerator.generateRentalReport(rent1));
        System.out.println(ReportGenerator.generateRentalReport(rent2));
        System.out.println();

        System.out.println(ReportGenerator.generatePaymentReport(pay1));
        System.out.println(ReportGenerator.generatePaymentReport(pay2));
        System.out.println();

    }
}
