package com.car_rental.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.exception.InvalidDataException;
import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;

public class CarFileParser {
    private static final Logger logger = LoggerFactory.getLogger(CarFileParser.class);

    public static Car parseCarFromLine(String line) {
        String[] parts = line.split(",");
        if(parts.length != 5){
            throw new InvalidDataException("Expected format 'licensePlate, model, year, mileage, status', got: " + line);
        }
        
        try {
            String licensePlate = parts[0].trim();
            String model = parts[1];
            int year = Integer.parseInt(parts[2].trim());
            double mileage = Double.parseDouble(parts[3].trim());
            CarStatus status = CarStatus.parseCarStatus(parts[4].trim());
            return new Car(licensePlate, model, year, mileage, status);

        } catch (NumberFormatException e) {
            throw new InvalidDataException("Invalid number in line " + e.getMessage());
        }
    }

    public static List<Car> parseFromCSV(String filePath) {
        List<Car> cars = new ArrayList<>();
        Path path = Path.of(filePath);

        if(!Files.exists(path)){
            throw new InvalidDataException("File not found at " + filePath);
        }

        logger.info("Trying to parse from file: {}", filePath);
        try{
            List<String> lines = Files.readAllLines(path, Charset.forName("windows-1251"));

            for(int i = 0; i< lines.size(); i++){
                String line = lines.get(i);
                if (line.isEmpty() || line.startsWith("#")) continue;

                try {
                    Car car = parseCarFromLine(line);
                    cars.add(car);
                    logger.info("Parsed car from line {}: {}", i, car);
                } catch (InvalidDataException e) {
                    logger.error("Failed to parse from line {}: {}", i, e.getMessage());
                }
        }
        } catch(IOException e){
            logger.error("Error while reading from file {}", filePath);
        }
        

        logger.info("Successfully parsed {} cars from file", cars.size());
        return cars;
    }
}
