package com.car_rental;

import java.util.List;

import com.car_rental.model.Branch;
import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;
import com.car_rental.model.Customer;
import com.car_rental.model.Payment;
import com.car_rental.model.PaymentMethod;
import com.car_rental.model.Rental;
import com.car_rental.parser.CarFileParser;
import com.car_rental.repository.BranchRepository;
import com.car_rental.repository.CarRepository;
import com.car_rental.repository.GenericRepository;
import com.car_rental.repository.PaymentRepository;
import com.car_rental.service.ReportGenerator;

public class Main {
    public static void main(String[] args) {
        CarRepository cars = new CarRepository();
        cars.add(new Car("СЕ1234ЕК", "Toyota Corolla", 2019, 30000, CarStatus.AVAILABLE));
        cars.add(new Car("АА5678КС", "Ford Focus",   2016, 55000, CarStatus.AVAILABLE));
        cars.add(new Car("АТ9012МК", "Hyundai i30",  2021, 15000, CarStatus.MAINTENANCE));
        BranchRepository branches = new BranchRepository();
        branches.add(new Branch("123", "123"));
        System.out.println(branches.findByLocation("123"));
        GenericRepository<Customer> customers = new GenericRepository<>(Customer::driverLicense, "Customer");

        customers.add(new Customer("Olena", "Shevchenko", "ХЕН123456", "12.05.1992"));
        customers.add(new Customer("Nikita", "Kovalenko", "ХНВ123456", "03.11.1988"));

        Rental rent1 = new Rental("0", cars.get(0), customers.get(0), "01.09.2025", "05.09.2025");
        Rental rent2 = new Rental("1", cars.get(1), customers.get(0)); // default endDate = now+10 days

        PaymentRepository payments = new PaymentRepository();
        payments.add(new Payment("1", rent2, 1000, PaymentMethod.CASH));
        //System.out.println(payments.findByIdentity("1").orElse(null).toString());

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
