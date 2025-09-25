package com.car_rental.util;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.car_rental.exception.InvalidDataException;

public class CarUtilTest {
    @ParameterizedTest
    @ValueSource(strings = {
        "КА8838ЕН",
        "license"
    })
    @DisplayName("Should return true for valid license plates")
    void testValidLicensePlates(String plate) {
        boolean actual = CarUtil.isValidLicensePlate(plate);
        assertTrue(actual,
         () -> String.format("Expected license plate %s to be valid, but was invalid", plate));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "8+lettersword",
        "ZVtest"
    })
    @DisplayName("Should return false for invalid license plates")
    void testInvalidLicensePlates(String plate) {
        boolean actual = CarUtil.isValidLicensePlate(plate);
        assertFalse(actual,
         () -> String.format("Expected license plate %s to be invalid, but was valid", plate));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 10.5, 1000000})
    @DisplayName("Should return true for valid mileage values")
    void testValidMileage(double mileage) {
        assertTrue(CarUtil.isValidMileage(mileage));
    }

    @Test
    @DisplayName("Should return false for negative mileage values")
    void testInvalidMileage() {
        assertFalse(CarUtil.isValidMileage(-1.0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ModelX", "Toyota Corolla",})
    @DisplayName("Should return true for valid model names")
    void testIsValidModel(String model) {
        assertTrue(CarUtil.isValidModel(model));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "abc"})
    @DisplayName("Should return false for invalid model names")
    void testIsValidModelInvalid(String model) {
        assertFalse(CarUtil.isValidModel(model));
    }

    @Test
    @DisplayName("Should return true for valid years")
    void testIsValidYear() {
        assertTrue(CarUtil.isValidYear(LocalDate.now().getYear() - 40));
        assertTrue(CarUtil.isValidYear(LocalDate.now().getYear()));
        assertTrue(CarUtil.isValidYear(LocalDate.now().getYear() - 20));
    }

    @Test
    @DisplayName("Should return false for invalid years")
    void testIsValidYearInvalid() {
        assertFalse(CarUtil.isValidYear(LocalDate.now().getYear() - 41));
        assertFalse(CarUtil.isValidYear(LocalDate.now().getYear() + 1));
    }

    @Test
    @DisplayName("validateLicensePlate throws for invalid plates")
    void testValidateLicensePlate() {
        assertThrows(InvalidDataException.class, () -> CarUtil.validateLicensePlate("toolongplate"));
        assertDoesNotThrow(() -> CarUtil.validateLicensePlate("КА8838ЕН"));
    }

    @Test
    @DisplayName("validateMileage throws for negative values")
    void testValidateMileage() {
        assertThrows(InvalidDataException.class, () -> CarUtil.validateMileage(-1.0));
        assertDoesNotThrow(() -> CarUtil.validateMileage(0));
    }

    @Test
    @DisplayName("validateModel throws for invalid names")
    void testValidateModel() {
        assertThrows(InvalidDataException.class, () -> CarUtil.validateModel(""));
        assertDoesNotThrow(() -> CarUtil.validateModel("Toyota Corolla"));
    }

    @Test
    @DisplayName("validateYear throws for invalid years")
    void testValidateYear() {
        assertThrows(InvalidDataException.class, () -> CarUtil.validateYear(LocalDate.now().getYear() - 41));
        assertDoesNotThrow(() -> CarUtil.validateYear(LocalDate.now().getYear()));
    }
}
