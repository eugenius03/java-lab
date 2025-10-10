package com.car_rental.repository;

import java.util.List;

import com.car_rental.model.Payment;

public class PaymentRepository  extends GenericRepository<Payment> {

    public PaymentRepository(){
        super(payment -> String.valueOf(payment.getId()), "Payment");
    }

    public List<Payment> sortByPaymentDate(){
        return sortByComparator(Payment.byPaymentDate());
    }

    public List<Payment> sortByAmount(){
        return sortByComparator(Payment.byAmount());
    }
}
