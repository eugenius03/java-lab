package com.car_rental.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.config.AppConfig;
import com.car_rental.exception.DataSerializationException;
import com.car_rental.serializer.DataSerializer;
import com.car_rental.serializer.JsonDataSerializer;
import com.car_rental.serializer.YamlDataSerializer;

public class PersistenceManager {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceManager.class);

    private final AppConfig config;
    private final Map<String, DataSerializer<?>> serializers;

    public PersistenceManager(AppConfig config) {
        this.config = config;
        this.serializers = new HashMap<>();
        initializeSerializers();
        logger.info("RepositoryManager initialized with {} serializers", serializers.size());
    }

    private void initializeSerializers() {
        serializers.put("JSON", new JsonDataSerializer<>());
        serializers.put("YAML", new YamlDataSerializer<>());
        logger.debug("Registered serializers: {}", serializers.keySet());
    }

    public <T> void save(List<T> items,
                         String entityType,
                         Class<T> clazz,
                         String format) throws DataSerializationException {
        validateParameters(entityType, clazz);

        if (items == null) {
            throw new DataSerializationException("Items list cannot be null");
        }

        String formatUpper = format.toUpperCase();
        DataSerializer<T> serializer = getSerializer(formatUpper);
        String filePath = getFilePath(entityType, formatUpper);

        logger.info("Saving {} items of type {} to {} file: {}",
                items.size(), entityType, formatUpper, filePath);

        try {
            serializer.serialize(items, filePath);
            logger.info("Successfully saved {} {} items to {}", items.size(), entityType, formatUpper);
        } catch (DataSerializationException e) {
            logger.error("Failed to save {} to {}: {}", entityType, formatUpper, e.getMessage());
            throw e;
        }
    }

    public <T> List<T> load(String entityType,
                            Class<T> clazz,
                            String format) throws DataSerializationException {

        validateParameters(entityType, clazz);

        String formatUpper = format.toUpperCase();
        DataSerializer<T> serializer = getSerializer(formatUpper);
        String filePath = getFilePath(entityType, formatUpper);

        logger.info("Loading {} from {} file: {}", entityType, formatUpper, filePath);

        try {
            List<T> items = serializer.deserialize(filePath, clazz);
            logger.info("Successfully loaded {} items of type {}", items.size(), entityType);
            return items;
        } catch (DataSerializationException e) {
            logger.error("Failed to load {} from {}: {}", entityType, formatUpper, e.getMessage());
            throw e;
        }
    }

    public <T> void saveAllFormats(List<T> items,
                                   String entityType,
                                   Class<T> clazz) throws DataSerializationException {
        logger.info("Saving {} items of type {} to all formats", items.size(), entityType);

        save(items, entityType, clazz, "JSON");
        save(items, entityType, clazz, "YAML");

        logger.info("Successfully saved {} to all formats", entityType);
    }

    private <T> void validateParameters(String entityType, Class<T> clazz)
            throws DataSerializationException {

        if (entityType == null || entityType.trim().isEmpty()) {
            throw new DataSerializationException("Entity type cannot be null or empty");
        }

        if (clazz == null) {
            throw new DataSerializationException("Class type cannot be null");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> DataSerializer<T> getSerializer(String format) throws DataSerializationException {
        DataSerializer<?> serializer = serializers.get(format);

        if (serializer == null) {
            String errorMsg = String.format("Unsupported format: %s. Available formats: %s",
                    format, serializers.keySet());
            logger.error(errorMsg);
            throw new DataSerializationException(errorMsg);
        }

        return (DataSerializer<T>) serializer;
    }

    private String getFilePath(String entityType, String format) {
        return switch (format) {
            case "JSON" -> config.getJsonFilePath(entityType);
            case "YAML" -> config.getYamlFilePath(entityType);
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }
}
