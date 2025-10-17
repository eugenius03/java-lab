package com.car_rental.repository;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.model.Payment;

public class PaymentRepository  extends GenericRepository<Payment> {
    private static final Logger logger = LoggerFactory.getLogger(PaymentRepository.class);


    public PaymentRepository(){
        super(payment -> String.valueOf(payment.getId()), "Payment");
    }

    public List<Payment> sortByPaymentDate(){
        return sortByComparator(Payment.byPaymentDate());
    }

    public List<Payment> sortByAmount(){
        return sortByComparator(Payment.byAmount());
    }

    public Optional<Payment> findById(String id){
        if(id == null){
            logger.warn(String.format("findById called with null id"));
            return Optional.empty();
        }
        logger.info(String.format("Trying to find Payment by %s", id));
        return findByIdentity(id);
    }

    public List<Payment> findByRentalId(String id){
        if(id == null){
            logger.warn(String.format("findByRentalId called with null id"));
            return Collections.emptyList();
        }
        logger.info(String.format("Trying to find Payment by Rental %s", id));
        return findByPredicate(x -> x.getRental().getId().toLowerCase().equals(id.trim().toLowerCase()))
            .toList();
    }
}
