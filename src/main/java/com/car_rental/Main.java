package com.car_rental;

import com.car_rental.model.Branch;
import com.car_rental.model.Car;
import com.car_rental.model.Customer;
import com.car_rental.model.Payment;
import com.car_rental.model.Rental;

public class Main {
    public static void main(String[] args) {
        Branch branch = new Branch("main", "Chernivtsi");
        Car car = new Car("СЕ1923КН", "Mercedes-Benz Sprinter", 2000, 999999.9);
        Customer customer = new Customer("Vasyl", "Vodii", "ВХН818233", "01.01.1999");
        Rental rental = new Rental(car, customer, "12.09.2025", "13.09.2025");
        Rental rental2 = new Rental(car, customer);
        Payment payment = new Payment(rental, 100.0, "12.09.2025");
        Payment payment2 = new Payment(rental, 100.0);
        Car nullCar = new Car("ЇФ4023ЩГ", "sdfjfsj", 1990, -1.0);

        System.out.println(branch + "\n");
        System.out.println(car + "\n");
        System.out.println(customer + "\n");
        System.out.println(rental + "\n");
        System.out.println(rental2 + "\n");
        System.out.println(payment + "\n");
        System.out.println(payment2 + "\n");
        System.out.println(nullCar);
    }
}