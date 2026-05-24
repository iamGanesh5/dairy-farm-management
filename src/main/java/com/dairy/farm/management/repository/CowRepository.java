package com.dairy.farm.management.repository;

import com.dairy.farm.management.entity.Cow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * Repository interface for Cow entity.
 * Handles database operations related to cows.
 */

@Repository
public interface CowRepository
        extends JpaRepository<Cow, Long> {

    /*
     * Find cow by tag number.
     */
    Optional<Cow> findByTagNumber(
            String tagNumber);

    /*
     * Find cow by cow name
     * and owner name.
     */
    Optional<Cow>
    findByCowNameAndOwnerOwnerName(
            String cowName,
            String ownerName);

    /*
     * Count cows by owner id.
     */
    Long countByOwnerId(Long ownerId);

}