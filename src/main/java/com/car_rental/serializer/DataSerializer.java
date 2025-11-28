package com.car_rental.serializer;

import com.car_rental.exception.DataSerializationException;

import java.util.List;

public interface DataSerializer<T> {

    void serialize(List<T> items, String filePath) throws DataSerializationException;

    List<T> deserialize(String filePath, Class<T> clazz) throws DataSerializationException;

    String toString(T item);

    String listToString(List<T> items);

    T fromString(String str, Class<T> clazz);

    String getFormat();

}
