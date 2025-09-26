package com.car_rental.parser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.car_rental.exception.InvalidDataException;
import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;

class CarFileParserTest {

    @Test
    @DisplayName("Should parse car from valid line")
    void testParseCarFromValidLine() {
        String line = "СЕ9999КН,Mercedes-Benz Vito,2000,77777,AVAILABLE";
        Car car = CarFileParser.parseCarFromLine(line);
        assertAll(
            () -> assertEquals("СЕ9999КН", car.getLicensePlate()),
            () -> assertEquals("Mercedes-Benz Vito", car.getModel()),
            () -> assertEquals(2000, car.getYear()),
            () -> assertEquals(77777.0, car.getMileage()),
            () -> assertEquals(CarStatus.AVAILABLE, car.getStatus())
        );
    }

    @Test
    @DisplayName("Should throw exception for line with wrong column count")
    void testParseCarFromLineWrongColumnCount() {
        String line = "only, four, columns, here";
        assertThrows(InvalidDataException.class,
                () -> CarFileParser.parseCarFromLine(line));
    }

    @Test
    @DisplayName("Should throw exception for line with invalid year or mileage")
    void testParseCarFromLineInvalidNumber() {
        String line = "СЕ9999КН, ModelX, not_a_year, 77777, AVAILABLE";
        assertThrows(InvalidDataException.class,
                () -> CarFileParser.parseCarFromLine(line));

        String line2 = "СЕ9999КН, ModelX, 2000, notAMileage, AVAILABLE";
        assertThrows(InvalidDataException.class,
                () -> CarFileParser.parseCarFromLine(line2));
    }

    @Test
    @DisplayName("Should throw InvalidDataException for non-existent file")
    void testParseFromCSVNonExistentFile() {
        assertThrows(InvalidDataException.class,
                () -> CarFileParser.parseFromCSV("missing.csv"));
    }

    @Test
    @DisplayName("Should parse multiple cars from CSV file")
    void testParseFromCSVValid(@TempDir Path tempDir) throws Exception {
        Path csvFile = tempDir.resolve("test.csv");
        List<String> lines = List.of(
                "CE9999ET, Mercedes-Benz Vito, 2000, 77777.0, AVAILABLE",
                "AT8838HI,Opel Astra, 2010, 123456.0, RESERVED"
        );
        Files.write(csvFile, lines);

        List<Car> cars = CarFileParser.parseFromCSV(csvFile.toString());
        assertEquals(2, cars.size());

        assertEquals("CE9999ET", cars.get(0).getLicensePlate());
        assertEquals(CarStatus.AVAILABLE, cars.get(0).getStatus());

        assertEquals("AT8838HI", cars.get(1).getLicensePlate());
        assertEquals("Opel Astra", cars.get(1).getModel());
        assertEquals(CarStatus.RESERVED, cars.get(1).getStatus());
    }

    @Test
    @DisplayName("Should skip empty and comment lines in CSV")
    void testParseFromCSVSkipEmptyLines(@TempDir Path tempDir) throws Exception {
        Path csvFile = tempDir.resolve("test.csv");
        List<String> lines = List.of(
                "",
                "# This is a comment",
                "KA8838EH, Opel Astra, 2010, 123456.0, RESERVED"
        );
        Files.write(csvFile, lines);

        List<Car> cars = CarFileParser.parseFromCSV(csvFile.toString());
        assertEquals(1, cars.size());
    }

    @Test
    @DisplayName("Should parse only valid lines and log error for invalid")
    void testParseFromCSVWithInvalidLines(@TempDir Path tempDir) throws Exception {
        Path csvFile = tempDir.resolve("test.csv");
        List<String> lines = List.of(
                "license,Honda Civic, 2000, 1333.0, RESERVED",
                "not enough columns"
        );
        Files.write(csvFile, lines);

        List<Car> cars = CarFileParser.parseFromCSV(csvFile.toString());
        assertEquals(1, cars.size());
        assertEquals("license", cars.get(0).getLicensePlate());
    }
}
