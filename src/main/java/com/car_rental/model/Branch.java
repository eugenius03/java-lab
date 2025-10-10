package com.car_rental.model;

import java.util.Comparator;

import com.car_rental.util.BranchUtil;

public record Branch (String name, String location) implements Comparable<Branch> {

    public Branch {
        BranchUtil.validateName(name);
        BranchUtil.validateLocation(location);
    }

    @Override
    public int compareTo (Branch o){
        return this.name().compareTo(o.name());
    }

    public static Comparator<Branch> byLocation(){
        return Comparator.comparing(Branch::location);
    }


}
