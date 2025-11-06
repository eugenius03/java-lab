package com.car_rental.serializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;

class JsonDataSerializerTest {
    
    private final JsonDataSerializer<Car> serializer = new JsonDataSerializer<>();
    
    @Test
    void testSerializeAndDeserializeCars(@TempDir Path tempDir) throws Exception {
        // Arrange
        List<Car> cars = Arrays.asList(
            new Car("СЕ1234ЕК", "Toyota Corolla", 2019, 30000, CarStatus.AVAILABLE),
            new Car("АА5678КС", "Ford Focus", 2016, 55000, CarStatus.RENTED)
        );
        Path filePath = tempDir.resolve("test_cars.json");
        
        // Act
        serializer.serialize(cars, filePath.toString());
        List<Car> deserializedCars = serializer.deserialize(filePath.toString(), Car.class);
        
        // Assert
        assertNotNull(deserializedCars);
        assertEquals(2, deserializedCars.size());
        assertEquals("СЕ1234ЕК", deserializedCars.get(0).getLicensePlate());
        assertEquals("Ford Focus", deserializedCars.get(1).getModel());
        assertEquals(2019, deserializedCars.get(0).getYear());
        assertEquals(CarStatus.AVAILABLE, deserializedCars.get(0).getStatus());
    }
    
    
    @Test
    void testSerializeEmptyList(@TempDir Path tempDir) throws Exception {
        // Arrange
        Path filePath = tempDir.resolve("empty.json");
        
        // Act
        serializer.serialize(List.of(), filePath.toString());
        List<Car> result = serializer.deserialize(filePath.toString(), Car.class);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testDeserializeNonExistentFile() {
        // Act & Assert
        assertThrows(Exception.class, () -> 
            serializer.deserialize("nonexistent_file.json", Car.class)
        );
    }
    
    @Test
    void testSerializeCreatesFile(@TempDir Path tempDir) throws Exception {
        // Arrange
        List<Car> cars = List.of(
            new Car("АТ9012МК", "Hyundai i30", 2021, 15000, CarStatus.MAINTENANCE)
        );
        Path filePath = tempDir.resolve("new_cars.json");
        
        // Act
        serializer.serialize(cars, filePath.toString());
        
        // Assert
        assertTrue(Files.exists(filePath));
        assertTrue(Files.size(filePath) > 0);
    }
    
    @Test
    void testSerializeWithSpecialCharacters(@TempDir Path tempDir) throws Exception {
        // Arrange
        List<Car> cars = List.of(
            new Car("СЕ1234ЕК", "Toyota Corolla™", 2019, 30000, CarStatus.AVAILABLE)
        );
        Path filePath = tempDir.resolve("special_chars.json");
        
        // Act
        serializer.serialize(cars, filePath.toString());
        List<Car> result = serializer.deserialize(filePath.toString(), Car.class);
        
        // Assert
        assertEquals("Toyota Corolla™", result.get(0).getModel());
    }
    
    @Test
    void testDeserializeInvalidJson(@TempDir Path tempDir) throws Exception {
        // Arrange
        Path filePath = tempDir.resolve("invalid.json");
        Files.writeString(filePath, "{invalid json content}");
        
        // Act & Assert
        assertThrows(Exception.class, () -> 
            serializer.deserialize(filePath.toString(), Car.class)
        );
    }
}