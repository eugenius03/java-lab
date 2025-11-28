package com.car_rental.model;

import java.util.Comparator;
import java.util.Objects;

import com.car_rental.util.ValidationUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class Car implements Comparable<Car> {

    @NotBlank(message = "License plate cannot be null or blank")
    @Pattern(
            regexp = "^[АВЕКМНОРСТІХDIPYCE]{2}\\\\d{4}[АВЕКМНОРСТІХDIPYCE]{2}$|^(?!.*[ZV])[а-яА-ЯЇЄІҐїєіїa-zA-Z0-9]{3,8}$",
            message = "License plate must match standard or individual format"
    )
    @JsonProperty("license_plate")
    private String licensePlate;

    @NotBlank(message = "Car model cannot be null or blank")
    @Size(min = 5, max = 100, message = "Car model must be 5-100 characters long")
    private String model;

    @Positive(message = "Year must be a positive number")
    private int year;

    @Positive(message = "Mileage must be a positive number")
    private double mileage;

    @NotNull(message = "Car status cannot be null")
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
        this.licensePlate = licensePlate;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.status = status;

        ValidationUtil.validate(this);
    }

    public Car(String licensePlate, String model, int year, double mileage){
        this.licensePlate = licensePlate;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.status = CarStatus.AVAILABLE;

        ValidationUtil.validate(this);

    }

    public Car(String licensePlate, String model, int year, double mileage, String status){
        this.licensePlate = licensePlate;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.status = CarStatus.AVAILABLE;
        this.status = CarStatus.parseCarStatus(status);

        ValidationUtil.validate(this);

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
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate(){
        return licensePlate;
    }

    public void setModel(String model){
        this.model = model;
    }

    public String getModel(){
        return model;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public void setMileage(double mileage){
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
        Car car = new Car(licensePlate, model, year, mileage, status);
        ValidationUtil.validate(car);
        return car;
    }

    public static Car createCar(String licensePlate, String model, int year, double mileage){
        Car car = new Car(licensePlate, model, year, mileage);
        ValidationUtil.validate(car);
        return car;
    }

    public static Car createCar(String licensePlate, String model, int year, double mileage, String status){
        Car car = new Car(licensePlate, model, year, mileage, status);
        ValidationUtil.validate(car);
        return car;
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
