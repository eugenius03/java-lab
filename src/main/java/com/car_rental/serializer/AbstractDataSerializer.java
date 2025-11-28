package com.car_rental.serializer;

import com.car_rental.exception.DataSerializationException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataSerializer<T> implements DataSerializer<T> {

    protected final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(AbstractDataSerializer.class);

    protected AbstractDataSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void serialize(List<T> items, String filePath) throws DataSerializationException {
        validateItemsForSerialization(items);
        validateFilePath(filePath);

        try {
            File file = new File(filePath);
            createParentDirectories(file);
            objectMapper.writeValue(file, items);
            logger.info("Successfully serialized {} items to {} file: {}",
                    items.size(), getFormat(), filePath);

        } catch (IOException e) {
            String errorMsg = String.format("Failed to serialize data to %s file: %s",
                    getFormat(), filePath);
            throw new DataSerializationException(errorMsg, e);
        }
    }

    @Override
    public List<T> deserialize(String filePath, Class<T> clazz) throws DataSerializationException {
        validateFilePath(filePath);
        validateClass(clazz);

        try {
            File file = new File(filePath);

            if (!file.exists()) {
                logger.warn("File does not exist: {}.", filePath);
                throw new DataSerializationException("File not found: " + filePath);
            }

            if (file.length() == 0) {
                logger.warn("File is empty: {}. Returning empty list.", filePath);
                return new ArrayList<>();
            }

            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> items = objectMapper.readValue(file, type);

            if (items == null) {
                items = new ArrayList<>();
            }

            logger.info("Successfully deserialized {} items from {} file: {}",
                    items.size(), getFormat(), filePath);
            return items;

        } catch (IOException e) {
            String errorMsg = String.format("Failed to deserialize data from %s file: %s",
                    getFormat(), filePath);
            throw new DataSerializationException(errorMsg, e);
        }
    }

    @Override
    public String toString(T item) throws DataSerializationException {
        if (item == null) {
            throw new DataSerializationException("Cannot serialize null item");
        }

        try {
            String result = objectMapper.writeValueAsString(item);
            logger.debug("Serialized single item to {} string", getFormat());
            return result;
        } catch (IOException e) {
            String errorMsg = String.format("Failed to serialize item to %s string", getFormat());
            throw new DataSerializationException(errorMsg, e);
        }
    }

    @Override
    public String listToString(List<T> items) throws DataSerializationException {
        if (items == null) {
            throw new DataSerializationException("Cannot serialize null list");
        }

        try {
            String result = objectMapper.writeValueAsString(items);
            logger.debug("Serialized {} items to {} string", items.size(), getFormat());
            return result;
        } catch (IOException e) {
            String errorMsg = String.format("Failed to serialize list to %s string", getFormat());
            throw new DataSerializationException(errorMsg, e);
        }
    }

    @Override
    public T fromString(String str, Class<T> clazz) throws DataSerializationException {
        if (str == null || str.trim().isEmpty()) {
            throw new DataSerializationException("Cannot deserialize null or empty string");
        }

        validateClass(clazz);

        try {
            T result = objectMapper.readValue(str, clazz);
            logger.debug("Deserialized {} item from {} string", clazz.getSimpleName(), getFormat());
            return result;
        } catch (IOException e) {
            String errorMsg = String.format("Failed to deserialize from %s string", getFormat());
            throw new DataSerializationException(errorMsg, e);
        }
    }

    protected void validateItemsForSerialization(List<T> items) throws DataSerializationException {
        if (items == null) {
            throw new DataSerializationException("Cannot serialize null list");
        }
    }

    protected void validateFilePath(String filePath) throws DataSerializationException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new DataSerializationException("File path cannot be null or empty");
        }
    }

    protected void validateClass(Class<T> clazz) throws DataSerializationException {
        if (clazz == null) {
            throw new DataSerializationException("Class type cannot be null");
        }
    }

    protected void createParentDirectories(File file) {
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                logger.info("Created directory: {}", parentDir.getAbsolutePath());
            }
        }
    }
}
