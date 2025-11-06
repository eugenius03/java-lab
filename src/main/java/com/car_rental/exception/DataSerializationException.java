package com.car_rental.exception;

public class DataSerializationException extends RuntimeException{

    public DataSerializationException(){
        super();
    }

    public DataSerializationException(String message){
        super(message);
    }

    public DataSerializationException(Throwable e){
        super(e);
    }

    public DataSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
