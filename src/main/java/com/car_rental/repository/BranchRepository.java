package com.car_rental.repository;

import com.car_rental.model.Branch;

public class BranchRepository{

    private final GenericRepository<Branch> repository;

    public BranchRepository(){
        repository = new GenericRepository<>(Branch::name, "Branch");
    }

    
}
