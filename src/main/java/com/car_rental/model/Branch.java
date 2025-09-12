package com.car_rental.model;

import java.util.Objects;

import com.car_rental.util.BranchUtil;

public class Branch {
    private String name;
    private String location;

    public Branch() {

    }

    public Branch(String name, String location){
        setName(name);
        setLocation(location);


    }

    public void setName(String name){
        if (BranchUtil.isValidName(name)){
            this.name = name;
        }
    }

    public String getName(){
        return name;
    }

    public void setLocation(String location){
        if (BranchUtil.isValidLocation(location)){
            this.location = location;
        }
    }

    public static Branch createBranch(String name, String location){
        if (BranchUtil.isValidName(name) && BranchUtil.isValidLocation(location)){
            return new Branch(name, location);
        }
        return null;
    }

    @Override
    public String toString(){
        return String.format("Branch{name=%s, location=%s}",
             name, location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return  Objects.equals(name, branch.name) &&
                Objects.equals(location, branch.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }


}
