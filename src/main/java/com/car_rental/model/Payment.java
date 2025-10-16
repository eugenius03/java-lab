package com.car_rental.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

import com.car_rental.util.PaymentUtil;

public class Payment implements Comparable<Payment> {
    private String id;
    private Rental rental;
    private double amount;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;

    public Payment() {}

    public Payment(String id, Rental rental, double amount, String paymentDate, PaymentMethod paymentMethod) {
        setId(id);
        setRental(rental);
        rental.getCar().setStatus(CarStatus.RENTED);
        setAmount(amount);
        setPaymentDate(paymentDate);
        this.paymentMethod = paymentMethod;
    }

    public Payment(String id, Rental rental, double amount, String paymentDate, String paymentMethod) {
        setId(id);
        setRental(rental);
        rental.getCar().setStatus(CarStatus.RENTED);
        setAmount(amount);
        setPaymentDate(paymentDate);
        this.paymentMethod = PaymentMethod.parsePaymentMethod(paymentMethod);
    }

    public Payment(String id, Rental rental, double amount, PaymentMethod paymentMethod) {
        setId(id);
        setRental(rental);
        setAmount(amount);
        this.paymentDate = LocalDate.now();
        this.paymentMethod = paymentMethod;
    }

    public Payment(String id, Rental rental, double amount, String paymentMethod) {
        setId(id);
        setRental(rental);
        setAmount(amount);
        this.paymentDate = LocalDate.now();
        this.paymentMethod = PaymentMethod.parsePaymentMethod(paymentMethod);
    }

    @Override
    public int compareTo(Payment other){
        return this.getId().compareTo(other.getId());
    }

    public static Comparator<Payment> byPaymentDate(){
        return Comparator.comparing(Payment::getPaymentDate);
    }

    public static Comparator<Payment> byAmount(){
        return Comparator.comparing(Payment::getAmount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.trim();
    }

    public void setRental(Rental rental){
        rental.getCar().setStatus(CarStatus.RENTED);
        this.rental = rental;
    }

    public Rental getRental(){
        return rental;
    }

    public void setAmount(double amount){
        PaymentUtil.validateAmount(amount);
        this.amount = amount;
    }

    public double getAmount(){
        return amount;
    }

    public void setPaymentDate(String paymentDate){
        PaymentUtil.validateDateFormat(paymentDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.paymentDate = LocalDate.parse(paymentDate, formatter);
    }

    public LocalDate getPaymentDate(){
        return paymentDate;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Payment createPayment(String id, Rental rental, double amount, String paymentDate, PaymentMethod paymentMethod){
        return new Payment(id, rental, amount, paymentDate, paymentMethod);
    }

    public Payment createPayment(String id, Rental rental, double amount, String paymentDate, String paymentMethod){
        return new Payment(id, rental, amount, paymentDate, paymentMethod);
    }

    public Payment createPayment(String id, Rental rental, double amount, PaymentMethod paymentMethod){
        return new Payment(id, rental, amount, paymentMethod);
    }

    public Payment createPayment(String id, Rental rental, double amount, String paymentMethod){
        return new Payment(id, rental, amount, paymentMethod);
    }

    @Override
    public String toString(){
        return String.format("Payment[rental=%s, amount=%.2f, paymentDate=%s]",
             rental, amount, paymentDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return  Objects.equals(rental, payment.rental) &&
                amount == payment.amount &&
                Objects.equals(paymentDate, payment.paymentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rental, amount, paymentDate);
    }


}
