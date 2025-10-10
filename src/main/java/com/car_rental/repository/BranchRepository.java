package com.car_rental.repository;

import java.util.List;

import com.car_rental.model.Branch;

public class BranchRepository extends GenericRepository<Branch>{

    public BranchRepository(){
        super(Branch::name, "Branch");
    }

    public List<Branch> sortByLocation(){
        return sortByComparator(Branch.byLocation());
    }

    
}
