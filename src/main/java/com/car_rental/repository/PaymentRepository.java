package com.car_rental.repository;

import com.car_rental.model.Payment;

public class PaymentRepository {
    private final GenericRepository<Payment> repository;

    public PaymentRepository(){
        repository = new GenericRepository<>(payment -> String.valueOf(payment.getId()), "Payment");
    }
}
