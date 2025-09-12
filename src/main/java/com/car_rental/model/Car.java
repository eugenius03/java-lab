package com.car_rental.model;

import java.util.Objects;

import com.car_rental.util.CarUtil;

public class Car {
    private String licensePlate;
    private String model;
    private int year;
    private double mileage;

    public Car(){

    }

    public Car(String licensePlate, String model, int year, double mileage){
        setLicensePlate(licensePlate);
        setModel(model);
        setYear(year);
        setMileage(mileage);
    }

    public void setLicensePlate(String licensePlate){
        licensePlate = licensePlate.trim().toUpperCase();
        if (CarUtil.isValidLicensePlate(licensePlate)){
            this.licensePlate = licensePlate;
        }
    }

    public String getLicensePlate(){
        return licensePlate;
    }

    public void setModel(String model){
        if (CarUtil.isValidModel(model)){
            this.model = model;
        }
    }

    public String getModel(){
        return model;
    }

    public void setYear(int year){
        if(CarUtil.isValidYear(year)){
            this.year = year;
        }
    }

    public int getYear(){
        return year;
    }

    public void setMileage(double mileage){
        if(CarUtil.isValidMileage(mileage)){
            this.mileage = mileage;
        }
    }

    public double getMileage(){
        return mileage;
    }

    public static Car createCar(String licensePlate, String model, int year, double mileage){
        if (CarUtil.isValidLicensePlate(licensePlate) && CarUtil.isValidModel(model)
            && CarUtil.isValidYear(year) && CarUtil.isValidMileage(mileage)){
                return new Car(licensePlate, model, year, mileage);
            }

        return null;
    }

    @Override
    public String toString(){
        return String.format("Car{licensePlate=%s, model=%s, year=%d, mileage=%.1f}",
             licensePlate, model, year, mileage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return  Objects.equals(licensePlate, car.licensePlate) &&
                Objects.equals(model, car.model) &&
                year == car.year &&
                mileage == car.mileage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePlate, model, year, mileage);
    }
}
