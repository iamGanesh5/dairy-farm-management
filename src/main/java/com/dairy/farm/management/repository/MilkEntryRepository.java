package com.dairy.farm.management.repository;

import com.dairy.farm.management.entity.MilkEntry;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

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
            LocalDate endDate
    );

    /*
     * Fetch milk entries by cow id.
     */
    List<MilkEntry> findByCowId(
            Long cowId
    );

    /*
     * Fetch milk entries by owner name.
     */
    List<MilkEntry>
    findByCowOwnerOwnerName(
            String ownerName
    );

    /*
     * Fetch owner report between dates.
     */
    List<MilkEntry>
    findByCowOwnerOwnerNameAndEntryDateBetween(
            String ownerName,
            LocalDate startDate,
            LocalDate endDate
    );

    /*
     * Fetch by date.
     */
    List<MilkEntry>
    findByEntryDate(
            LocalDate entryDate
    );

    /*
     * Fetch today milk.
     */
    @Query("""

SELECT COALESCE(
SUM(
m.morningMilk +
m.eveningMilk
),0)

FROM MilkEntry m

WHERE m.cow.owner.id = :ownerId

AND m.entryDate = :date

""")
    Double getTodayMilk(

            @Param("ownerId")
            Long ownerId,

            @Param("date")
            LocalDate date

    );

    /*
     * Fetch monthly milk.
     */
    @Query("""

SELECT COALESCE(
SUM(
m.morningMilk +
m.eveningMilk
),0)

FROM MilkEntry m

WHERE m.cow.owner.id = :ownerId

AND MONTH(m.entryDate) = :month

AND YEAR(m.entryDate) = :year

""")
    Double getMonthlyMilk(

            @Param("ownerId")
            Long ownerId,

            @Param("month")
            int month,

            @Param("year")
            int year

    );

    /*
     * Fetch yearly milk.
     */
    @Query("""

SELECT COALESCE(
SUM(
m.morningMilk +
m.eveningMilk
),0)

FROM MilkEntry m

WHERE m.cow.owner.id = :ownerId

AND YEAR(m.entryDate) = :year

""")
    Double getYearlyMilk(

            @Param("ownerId")
            Long ownerId,

            @Param("year")
            int year

    );

    /*
     * Fetch monthly revenue.
     */
    @Query("""

SELECT COALESCE(
SUM(
(m.morningMilk + m.eveningMilk)
* m.pricePerLiter
),0)

FROM MilkEntry m

WHERE m.cow.owner.id = :ownerId

AND MONTH(m.entryDate) = :month

AND YEAR(m.entryDate) = :year

""")
    Double getMonthlyRevenue(

            @Param("ownerId")
            Long ownerId,

            @Param("month")
            int month,

            @Param("year")
            int year

    );

    /*
     * Fetch yearly revenue.
     */
    @Query("""

SELECT COALESCE(
SUM(
(m.morningMilk + m.eveningMilk)
* m.pricePerLiter
),0)

FROM MilkEntry m

WHERE m.cow.owner.id = :ownerId

AND YEAR(m.entryDate) = :year

""")
    Double getYearlyRevenue(

            @Param("ownerId")
            Long ownerId,

            @Param("year")
            int year

    );

}