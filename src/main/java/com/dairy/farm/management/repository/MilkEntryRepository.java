package com.dairy.farm.management.repository;

import com.dairy.farm.management.entity.MilkEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/*
 * Repository interface for MilkEntry entity.
 * Handles milk entry database operations.
 */

@Repository
public interface MilkEntryRepository
        extends JpaRepository<MilkEntry, Long> {

    /*
     * Fetch milk entries between dates.
     */
    List<MilkEntry> findByEntryDateBetween(
            LocalDate startDate,
            LocalDate endDate);

    /*
     * Fetch milk entries by cow id.
     */
    List<MilkEntry> findByCowId(Long cowId);
    List<MilkEntry>
    findByCowOwnerOwnerName(String ownerName);
    List<MilkEntry>
    findByCowOwnerOwnerNameAndEntryDateBetween(
            String ownerName,
            LocalDate startDate,
            LocalDate endDate);
    List<MilkEntry>
    findByEntryDate(
            LocalDate entryDate);
}