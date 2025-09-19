package com.car_rental.model;

import java.util.Objects;

import com.car_rental.util.CarUtil;

public class Car {
    private String licensePlate;
    private String model;
    private int year;
    private double mileage;
    private CarStatus status;

    public Car(){

    }

    public Car(String licensePlate, String model, int year, double mileage, CarStatus status){
        setModel(model);
        setLicensePlate(licensePlate);
        setYear(year);
        setMileage(mileage);
        this.status = status;
    }

    public Car(String licensePlate, String model, int year, double mileage){
        setLicensePlate(licensePlate);
        setModel(model);
        setYear(year);
        setMileage(mileage);
        this.status = CarStatus.AVAILABLE;
    }

    public Car(String licensePlate, String model, int year, double mileage, String status){
        setLicensePlate(licensePlate);
        setModel(model);
        setYear(year);
        setMileage(mileage);
        this.status = CarStatus.parseCarStatus(status);
    }

    public void setLicensePlate(String licensePlate){
        CarUtil.validateLicensePlate(licensePlate);
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate(){
        return licensePlate;
    }

    public void setModel(String model){
        CarUtil.validateModel(model);
        this.model = model;
    }

    public String getModel(){
        return model;
    }

    public void setYear(int year){
        CarUtil.validateYear(year);
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public void setMileage(double mileage){
        CarUtil.validateMileage(mileage);
        this.mileage = mileage;
    }

    public double getMileage(){
        return mileage;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public CarStatus getStatus() {
        return status;
    }

    public static Car createCar(String licensePlate, String model, int year, double mileage, CarStatus status){
        return new Car(licensePlate, model, year, mileage, status);
    }

    public static Car createCar(String licensePlate, String model, int year, double mileage){
        return new Car(licensePlate, model, year, mileage, CarStatus.AVAILABLE);
    }

    public static Car createCar(String licensePlate, String model, int year, double mileage, String status){
        return new Car(licensePlate, model, year, mileage, status);
    }

    @Override
    public String toString(){
        return String.format("Car[licensePlate=%s, model=%s, year=%d, mileage=%.1f]",
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
