package com.car_rental.model;

import java.util.Comparator;
import java.util.Objects;

import com.car_rental.util.CarUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Car implements Comparable<Car> {

    @JsonProperty("license_plate")
    private String licensePlate;
    private String model;
    private int year;
    private double mileage;
    private CarStatus status;

    public Car(){

    }

    @JsonCreator
    public Car(
        @JsonProperty("license_plate") String licensePlate,
        @JsonProperty("model") String model,
        @JsonProperty("year") int year,
        @JsonProperty("mileage") double mileage,
        @JsonProperty("status") CarStatus status
    ) {
        setLicensePlate(licensePlate);
        setModel(model);
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

    @Override
    public int compareTo(Car other){
        return Integer.compare(this.getStatusPriority(), other.getStatusPriority());
    }

    private int getStatusPriority() {
        return switch (status) {
            case AVAILABLE -> 1;
            case RESERVED -> 2;
            case RENTED -> 3;
            case MAINTENANCE -> 4;
        };
    }

    public static Comparator<Car> byYear() {
        return Comparator.comparing(Car::getYear);
    }

    public static Comparator<Car> byMileage() {
        return Comparator.comparing(Car::getMileage);
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
