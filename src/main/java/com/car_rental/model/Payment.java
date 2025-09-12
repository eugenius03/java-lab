package com.car_rental.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.car_rental.util.CustomerUtil;
import com.car_rental.util.PaymentUtil;

public class Payment {
    private Rental rental;
    private double amount;
    private LocalDate paymentDate;

    public Payment() {

    }

    public Payment(Rental rental, double amount, String paymentDate){
        this.rental = rental;
        setAmount(amount);
        setPaymentDate(paymentDate);
    }

    public Payment(Rental rental, double amount){
        this.rental = rental;
        setAmount(amount);
        this.paymentDate = LocalDate.now();
    }

    public void setRental(Rental rental){
        this.rental = rental;
    }

    public Rental getRental(){
        return rental;
    }

    public void setAmount(double amount){
        if(PaymentUtil.isValidAmount(amount)){
            this.amount = amount;
        }
    }

    public double getAmount(){
        return amount;
    }

    public void setPaymentDate(String paymentDate){
        if (CustomerUtil.isValidDateFormat(paymentDate)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            this.paymentDate = LocalDate.parse(paymentDate, formatter);
        }
    }

    public LocalDate getPaymentDate(){
        return paymentDate;
    }

    public Payment createPayment(Rental rental, double amount, String paymentDate){
        if(PaymentUtil.isValidAmount(amount) && PaymentUtil.isValidDateFormat(paymentDate)){
            return new Payment(rental, amount, paymentDate);
        }
        return null;
    }

    public Payment createPayment(Rental rental, double amount){
        if(PaymentUtil.isValidAmount(amount)){
            return new Payment(rental, amount);
        }
        return null;
    }

    @Override
    public String toString(){
        return String.format("Payment{rental=%s, amount=%.2f, paymentDate=%s}",
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
