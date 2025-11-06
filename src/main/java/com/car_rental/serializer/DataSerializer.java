package com.car_rental.serializer;

import java.util.List;

import com.car_rental.exception.DataSerializationException;

public interface DataSerializer<T> {
    
    void serialize(List<T> items, String filePath) throws DataSerializationException;

    List <T> deserialize(String filePath, Class<T> clazz) throws DataSerializationException;

    String getFormat();
}
