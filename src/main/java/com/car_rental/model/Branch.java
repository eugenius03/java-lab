package com.car_rental.model;

import com.car_rental.util.BranchUtil;

public record Branch (String name, String location) {

    public Branch {
        BranchUtil.validateName(name);
        BranchUtil.validateLocation(location);
    }


}
