package com.car_rental;

import java.util.List;

import com.car_rental.config.AppConfig;
import com.car_rental.model.Branch;
import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;
import com.car_rental.model.Customer;
import com.car_rental.model.Payment;
import com.car_rental.model.PaymentMethod;
import com.car_rental.model.Rental;
import com.car_rental.parser.CarFileParser;
import com.car_rental.persistence.PersistenceManager;
import com.car_rental.repository.BranchRepository;
import com.car_rental.repository.CarRepository;
import com.car_rental.repository.GenericRepository;
import com.car_rental.repository.PaymentRepository;
import com.car_rental.service.ReportGenerator;

public class Main {
    public static void main(String[] args) {
        
        AppConfig appConfig = new AppConfig();
        PersistenceManager persistenceManager = new PersistenceManager(appConfig);
        CarRepository cars = new CarRepository();
        cars.add(new Car("CE1234EK", "Toyota Corolla", 2019, 30000, CarStatus.AVAILABLE));
        cars.add(new Car("AA5678KC", "Ford Focus",   2016, 55000, CarStatus.AVAILABLE));
        cars.add(new Car("AT9012MK", "Hyundai i30",  2021, 15000, CarStatus.MAINTENANCE));

        persistenceManager.save(cars.getAll(), "cars", Car.class, "JSON");
        persistenceManager.load("cars", Car.class, "JSON")
        .stream().forEach(System.out::println);
        BranchRepository branches = new BranchRepository();
        branches.add(new Branch("Name", "Location"));
        System.out.println(branches.findByLocation("Location"));

        persistenceManager.save(branches.getAll(), "branches", Branch.class, "JSON");

        GenericRepository<Customer> customerRepository = new GenericRepository<>(Customer::driverLicense, "Customer");

        List<Customer> customers = persistenceManager.load("customers", Customer.class, "JSON");

        customerRepository.addAll(customers);
        customerRepository.add(new Customer("Olena", "Shevchenko", "ХЕН123456", "12.05.1992"));
        customerRepository.add(new Customer("Nikita", "Kovalenko", "ХНВ123456", "03.11.1988"));

        persistenceManager.saveAllFormats(customerRepository.getAll(), "customers", Customer.class);

        Rental rent1 = new Rental("0", cars.get(0), customerRepository.get(0), "01.09.2025", "05.09.2025");
        Rental rent2 = new Rental("1", cars.get(1), customerRepository.get(0)); // default endDate = now+10 days


        PaymentRepository payments = new PaymentRepository();
        payments.add(new Payment("1", rent2, 1000, PaymentMethod.CASH));

        persistenceManager.saveAllFormats(payments.getAll(), "payments", Payment.class);
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
