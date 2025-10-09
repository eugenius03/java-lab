package com.car_rental.repository;

@FunctionalInterface
public interface IdentityExtractor<T> {
    String extractIdentity(T object);
}
