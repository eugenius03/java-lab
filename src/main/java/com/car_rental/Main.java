package com.car_rental;

import java.util.List;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;
import com.car_rental.model.Customer;
import com.car_rental.model.Rental;
import com.car_rental.parser.CarFileParser;
import com.car_rental.repository.GenericRepository;
import com.car_rental.service.ReportGenerator;

public class Main {
    public static void main(String[] args) {
        GenericRepository<Car> cars = new GenericRepository<>(Car::getLicensePlate, "Car");
        cars.add(new Car("СЕ1234ЕК", "Toyota Corolla", 2019, 30000, CarStatus.AVAILABLE));
        cars.add(new Car("АА5678КС", "Ford Focus",   2016, 55000, CarStatus.AVAILABLE));
        cars.add(new Car("АТ9012МК", "Hyundai i30",  2021, 15000, CarStatus.MAINTENANCE));

        GenericRepository<Customer> customers = new GenericRepository<>(Customer::driverLicense, "Customer");

        customers.add(new Customer("Olena", "Shevchenko", "ХЕН123456", "12.05.1992"));
        customers.add(new Customer("Nikita", "Kovalenko", "ХНВ123456", "03.11.1988"));

        Rental rent1 = new Rental(0, cars.get(0), customers.get(0), "01.09.2025", "05.09.2025");
        Rental rent2 = new Rental(1, cars.get(1), customers.get(0)); // default endDate = now+10 days

        List<Car> fileCars = CarFileParser.parseFromCSV("cars.csv");
        cars.addAll(fileCars);

        for(Car car : cars.getAll()) {
            System.out.println(ReportGenerator.generateCarReport(car));
        }
        System.out.println();

        System.out.println(ReportGenerator.generateRentalReport(rent1));
        System.out.println(ReportGenerator.generateRentalReport(rent2));
        System.out.println();

    }
}
