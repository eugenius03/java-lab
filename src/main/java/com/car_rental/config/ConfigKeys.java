package com.car_rental.config;

public final class ConfigKeys {

    private ConfigKeys() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String DATA_PATH_BASE = "data.path.base";
    public static final String DATA_PATH_CARS_JSON = "data.path.cars.json";
    public static final String DATA_PATH_CARS_YAML = "data.path.cars.yaml";
    public static final String DATA_PATH_CUSTOMERS_JSON = "data.path.customers.json";
    public static final String DATA_PATH_CUSTOMERS_YAML = "data.path.customers.yaml";
    public static final String DATA_PATH_RENTALS_JSON = "data.path.rentals.json";
    public static final String DATA_PATH_RENTALS_YAML = "data.path.rentals.yaml";

    public static final String TEST_DATA_COUNT = "test.data.count";
}
