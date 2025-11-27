package com.car_rental.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.config.AppConfig;
import com.car_rental.model.Branch;
import com.car_rental.model.Car;
import com.car_rental.model.Customer;
import com.car_rental.model.Payment;
import com.car_rental.model.Rental;
import com.car_rental.persistence.PersistenceManager;
import com.car_rental.repository.BranchRepository;
import com.car_rental.repository.CarRepository;
import com.car_rental.repository.CustomerRepository;
import com.car_rental.repository.PaymentRepository;
import com.car_rental.repository.RentalRepository;
import com.car_rental.service.LoadResult;
import com.car_rental.service.loader.DataLoader;
import com.car_rental.service.loader.ParallelLoadingStrategy;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    private long startTime;

    @Override
    public void contextInitialized(ServletContextEvent event) {

        startTime = System.currentTimeMillis();
        ServletContext context = event.getServletContext();

        try {
            logger.info("Creating AppConfig and PersistenceManager...");
            AppConfig config = new AppConfig();
            PersistenceManager persistenceManager = new PersistenceManager(config);
            logger.info("AppConfig and PersistenceManager created successfully.");

            logger.info("Creating repositories...");
            BranchRepository branchRepository = new BranchRepository();
            CarRepository carRepository = new CarRepository();
            CustomerRepository customerRepository = new CustomerRepository();
            RentalRepository rentalRepository = new RentalRepository();
            PaymentRepository paymentRepository = new PaymentRepository();
            logger.info("Repositories created successfully.");

            logger.info("Loading data from JSON files...");
            DataLoader dataLoader = new DataLoader(persistenceManager);

            LoadResult loadResult = dataLoader.load(
                    branchRepository,
                    carRepository,
                    customerRepository,
                    rentalRepository,
                    new ParallelLoadingStrategy()
            );
            paymentRepository.addAll(persistenceManager.load("payments", Payment.class, "JSON"));

            logger.info("Data loading completed: " + loadResult);

            logger.info("Storing repositories and PersistenceManager in ServletContext...");
            context.setAttribute("branchRepository", branchRepository);
            context.setAttribute("carRepository", carRepository);
            context.setAttribute("customerRepository", customerRepository);
            context.setAttribute("rentalRepository", rentalRepository);
            context.setAttribute("paymentRepository", paymentRepository);
            context.setAttribute("persistenceManager", persistenceManager);
            logger.info("Repositories and PersistenceManager stored successfully.");

            long initTime = System.currentTimeMillis() - startTime;
            logger.info("Application context initialized in {} ms", initTime);
        } catch (Exception e) {
            logger.error("Error during application context initialization", e);
            throw new RuntimeException("Failed to initialize application context", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        try {
            System.out.println("========================================");
            System.out.println("APPLICATION SHUTDOWN - Starting cleanup");
            System.out.println("========================================");
            logger.info("Starting application context destruction...");
            
            BranchRepository branchRepository = (BranchRepository) context.getAttribute("branchRepository");
            CarRepository carRepository = (CarRepository) context.getAttribute("carRepository");
            CustomerRepository customerRepository = (CustomerRepository) context.getAttribute("customerRepository");
            PaymentRepository paymentRepository = (PaymentRepository) context.getAttribute("paymentRepository");
            RentalRepository rentalRepository = (RentalRepository) context.getAttribute("rentalRepository");

            PersistenceManager persistenceManager = (PersistenceManager) context.getAttribute("persistenceManager");

            if (persistenceManager != null) {
                logger.info("Saving data to JSON files before shutdown...");
                System.out.println("[SHUTDOWN] Saving data to JSON files...");

                if (branchRepository != null) {
                    persistenceManager.save(branchRepository.getAll(), "branches", Branch.class, "JSON");
                    logger.info("Branches saved successfully.");
                    System.out.println("[SHUTDOWN] Branches saved: " + branchRepository.getAll().size() + " items");
                }

                if (carRepository != null) {
                    persistenceManager.save(carRepository.getAll(), "cars", Car.class, "JSON");
                    logger.info("Cars saved successfully.");
                    System.out.println("[SHUTDOWN] Cars saved: " + carRepository.getAll().size() + " items");
                }

                if (customerRepository != null) {
                    persistenceManager.save(customerRepository.getAll(), "customers", Customer.class, "JSON");
                    logger.info("Customers saved successfully.");
                    System.out.println("[SHUTDOWN] Customers saved: " + customerRepository.getAll().size() + " items");
                }

                if (rentalRepository != null) {
                    persistenceManager.save(rentalRepository.getAll(), "rentals", Rental.class, "JSON");
                    logger.info("Rentals saved successfully.");
                    System.out.println("[SHUTDOWN] Rentals saved: " + rentalRepository.getAll().size() + " items");
                }

                logger.info("Data saved successfully");
                System.out.println("[SHUTDOWN] All data saved successfully!");

            }
            long uptime = System.currentTimeMillis() - startTime;
            logger.info("Application context destroyed. Uptime: {} ms", uptime);
            System.out.println("[SHUTDOWN] Application uptime: " + uptime + " ms");
            
            context.removeAttribute("branchRepository");
            context.removeAttribute("carRepository");
            context.removeAttribute("customerRepository");
            context.removeAttribute("paymentRepository");
            context.removeAttribute("rentalRepository");
            context.removeAttribute("persistenceManager");
            
            System.out.println("========================================");
            System.out.println("APPLICATION SHUTDOWN - Cleanup complete");
            System.out.println("========================================");
        } catch (Exception e) {
            logger.error("Error during application context destruction", e);
            System.err.println("[SHUTDOWN ERROR] " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Failed to destroy application context", e);
        }
    }
}
