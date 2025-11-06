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

class YamlDataSerializerTest {
    
    private final YamlDataSerializer<Car> serializer = new YamlDataSerializer<>();
    
    @Test
    void testSerializeAndDeserializeCars(@TempDir Path tempDir) throws Exception {
        // Arrange
        List<Car> cars = Arrays.asList(
            new Car("СЕ1234ЕК", "Toyota Corolla", 2019, 30000, CarStatus.AVAILABLE),
            new Car("АА5678КС", "Ford Focus", 2016, 55000, CarStatus.RENTED)
        );
        Path filePath = tempDir.resolve("test_cars.yaml");
        
        // Act
        serializer.serialize(cars, filePath.toString());
        List<Car> result = serializer.deserialize(filePath.toString(), Car.class);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("СЕ1234ЕК", result.get(0).getLicensePlate());
        assertEquals("Ford Focus", result.get(1).getModel());
        assertEquals(CarStatus.AVAILABLE, result.get(0).getStatus());
    }

    
    @Test
    void testSerializeEmptyList(@TempDir Path tempDir) throws Exception {
        // Arrange
        Path filePath = tempDir.resolve("empty.yaml");
        
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
            serializer.deserialize("nonexistent.yaml", Car.class)
        );
    }
    
    @Test
    void testSerializeCreatesFile(@TempDir Path tempDir) throws Exception {
        // Arrange
        List<Car> cars = Arrays.asList(
            new Car("СЕ1234ЕК", "Toyota Corolla", 2019, 30000, CarStatus.AVAILABLE)
        );
        Path filePath = tempDir.resolve("new_cars.yaml");

        // Act
        serializer.serialize(cars, filePath.toString());
        
        // Assert
        assertTrue(Files.exists(filePath));
        assertTrue(Files.size(filePath) > 0);
    }
    
    @Test
    void testYamlFormatIsReadable(@TempDir Path tempDir) throws Exception {
        // Arrange
        List<Car> cars = List.of(
            new Car("АТ9012МК", "Hyundai i30", 2021, 15000, CarStatus.MAINTENANCE)
        );
        Path filePath = tempDir.resolve("readable.yaml");
        
        // Act
        serializer.serialize(cars, filePath.toString());
        String content = Files.readString(filePath);
        
        // Assert
        assertTrue(content.contains("АТ9012МК"));
        assertTrue(content.contains("Hyundai i30"));
        assertTrue(content.contains("MAINTENANCE"));
    }
}