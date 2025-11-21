package com.car_rental.service;

public record LoadResult(
        int branchesLoaded,
        int carsLoaded,
        int customersLoaded,
        int rentalsLoaded,
        long durationMs
) {
    public int totalLoaded() {
        return branchesLoaded + carsLoaded + customersLoaded + rentalsLoaded;
    }

    @Override
    public String toString() {
        return String.format("LoadResult { branchesLoaded=%d, carsLoaded=%d, customersLoaded=%d," +
                        " rentalsLoaded=%d, totalLoaded=%d, durationMs=%d }",
                branchesLoaded, carsLoaded, customersLoaded, rentalsLoaded, totalLoaded(), durationMs);
    }
}
