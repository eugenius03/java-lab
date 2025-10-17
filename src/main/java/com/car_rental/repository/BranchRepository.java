package com.car_rental.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.model.Branch;

public class BranchRepository extends GenericRepository<Branch>{

    private static final Logger logger = LoggerFactory.getLogger(BranchRepository.class);


    public BranchRepository(){
        super(Branch::name, "Branch");
    }

    public List<Branch> sortByLocation(){
        return sortByComparator(Branch.byLocation());
    }

    public Optional<Branch> findByName(String name){
        if(name == null){
            logger.warn(String.format("findByName called with null name"));
            return Optional.empty();
        }
        logger.info(String.format("Trying to find Branch by %s", name));
        return findByIdentity(name);
    }

    public List<Branch> findByLocation(String location){
        if(location == null){
            logger.warn(String.format("findByLocation called with null location"));
            return Collections.emptyList();
        }
        logger.info(String.format("Trying to find Branch by %s", location));
        return findByPredicate(x -> x.location().trim().toLowerCase().contains(location.trim().toLowerCase()))
            .toList();
    }

    
}
