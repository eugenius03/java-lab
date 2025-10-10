package com.car_rental.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericRepository<T> {
    private static final Logger logger = LoggerFactory.getLogger(GenericRepository.class);
    
    private final List<T> items;
    private final IdentityExtractor<T> identityExtractor;
    private final String entityType;

    public GenericRepository(IdentityExtractor<T> identityExtractor, String entityType){
        this.items = new ArrayList<>();
        this.identityExtractor = identityExtractor;
        this.entityType = entityType;
        logger.info(String.format("Created repository for %s", entityType));
    }

    public boolean add(T item){
        if(item == null){
            logger.warn(String.format("Attempted to add null %s", entityType));
            return false;
        }

        String identity = identityExtractor.extractIdentity(item);
        if (findByIdentity(identity).isPresent()) {
            logger.warn(String.format("Cannot add %s - already exists with identity: ", identity));
            return false;
        }

        boolean added = items.add(item);
        if (added){
            logger.info(String.format("Added %s: %s", entityType, identity));
        }
        return added;
    }

    public boolean addAll(List<T> list){
        if(list == null){
            logger.warn(String.format("Attempted to add null %ss", entityType));
            return false;
        }

        boolean modified = false;
        for(T item: list){
            String identity = identityExtractor.extractIdentity(item);
            if (findByIdentity(identity).isPresent()) {
                logger.warn(String.format("Cannot add %s - already exists with identity: ", identity));
            } else{
                
                if (items.add(item)) {
                    modified = true;
                    logger.info(String.format("Added %s: %s", entityType, identity));
                }
                
            }
        }
        
        return modified;
    }

    public T get(int index){
        return items.get(index);
    }

    public List<T> sortByDefault(){
        if(items.isEmpty()){
            logger.warn(String.format("Attempted to sort an empty array %s", entityType));
            return new ArrayList<>();
        }
        logger.info(String.format("Successfully sorted %s by default", entityType));
        return items.stream().sorted().toList();

    }

    public List<T> sortByComparator(Comparator<T> comparator){
        if(items.isEmpty()){
            logger.warn(String.format("Attempted to sort an empty array %s", entityType));
            return new ArrayList<>();
        }

        if(comparator == null){
            logger.warn(String.format("Attempted to sort by null, returning the whole list %s", entityType));
            return new ArrayList<>(items);
        }

        logger.info(String.format("Successfully sorted %s by comparator", entityType));
        return items.stream().sorted(comparator).toList();

    }

    public List<T> sortByIdentity(String order){
        if(items.isEmpty()){
            logger.warn(String.format("Attempted to sort an empty array %s", entityType));
            return new ArrayList<>();
        }

        if(order == null){
            logger.warn(String.format("Attempted to sort by null, returning whole list %s", entityType));
            return new ArrayList<>(items);
        }

        Comparator<T> baseComparator = Comparator.comparing(
            identityExtractor::extractIdentity
        );
        
        Comparator<T> finalComparator = switch (order.trim().toLowerCase()) {
            case "asc", "ascending" -> {
                logger.info(String.format("Sorted %s by identity in ascending order", entityType));
                yield baseComparator;
            }
            case "desc", "descending" -> {
                logger.info(String.format("Sorted %s by identity in descending order", entityType));
                yield baseComparator.reversed();
            }
            default -> {
                logger.warn(String.format("Invalid sort order '%s' for %s. Using ascending order.", order, entityType));
                yield baseComparator;
            }
        };
        
        return items.stream()
            .sorted(finalComparator)
            .collect(Collectors.toList());
    }

    public boolean remove(T item){
        if(item == null){
            logger.warn(String.format("Attempted to remove null %s", entityType));
            return false;
        }

        boolean removed = items.remove(item);
        if (removed) {
            logger.info(String.format("Removed %s: %s", entityType, identityExtractor.extractIdentity(item)));
        } else {
            logger.warn(String.format("Failed to remove %s: %s", entityType, identityExtractor.extractIdentity(item)));
        }
        return removed;
    }

    public boolean removeByIdentity(String identity) {
        if (identity == null) {
            logger.warn(String.format("Attempted to remove %s with null identity", entityType));
            return false;
        }

        Optional<T> itemToRemove = items.stream()
                .filter(item -> identity.equals(identityExtractor.extractIdentity(item)))
                .findFirst();

        if (itemToRemove.isPresent()) {
            boolean removed = items.remove(itemToRemove.get());
            if (removed) {
                logger.info(String.format("Removed %s by identity: %s", entityType, identity));
            }
            return removed;
        } else {
            logger.warn(String.format("No %s found with identity: %s to remove", entityType, identity));
            return false;
        }
    }

    public boolean contains(T item) {
        return items.contains(item);
    }

    public boolean containsIdentity(String identity) {
        return findByIdentity(identity).isPresent();
    }

    public Optional<T> findByIdentity(String identity) {
        if (identity == null) {
            logger.warn(String.format("Attempted to find %s with null identity", entityType));
            return Optional.empty();
        }

        Optional<T> result = items.stream()
                .filter(item -> identity.equals(identityExtractor.extractIdentity(item)))
                .findFirst();

        if (result.isPresent()) {
            logger.info(String.format("Found %s with identity: %s", entityType, identity));
        } else {
            logger.info(String.format("No %s found with identity: %s", entityType, identity));
        }

        return result;
    }

    public List<T> getAll() {
        logger.info(String.format("Retrieved all %s items. Count: %d", entityType, items.size()));
        return new ArrayList<>(items);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        int sizeBefore = items.size();
        items.clear();
        logger.info(String.format("Cleared repository. Removed %d %s items", sizeBefore, entityType));
    }
    
}
