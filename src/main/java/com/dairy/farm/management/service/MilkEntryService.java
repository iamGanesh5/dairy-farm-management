package com.dairy.farm.management.service;

import com.dairy.farm.management.dto.MilkEntryRequestDTO;
import com.dairy.farm.management.dto.MonthlyOwnerMilkReportDTO;
import com.dairy.farm.management.dto.OwnerSummaryDTO;
import com.dairy.farm.management.entity.Cow;
import com.dairy.farm.management.entity.MilkEntry;
import com.dairy.farm.management.exception.ResourceNotFoundException;
import com.dairy.farm.management.repository.CowRepository;
import com.dairy.farm.management.repository.MilkEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Service class for handling
 * milk entry business logic.
 */

@Service
@RequiredArgsConstructor
public class MilkEntryService {

    private final MilkEntryRepository milkEntryRepository;
    private final CowRepository cowRepository;

    /*
     * Add daily milk entry
     * using cow id.
     */
    public MilkEntry addMilkEntry(
            Long cowId,
            MilkEntry milkEntry) {

        // Fetch cow details
        Cow cow = cowRepository.findById(cowId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cow not found with id : "
                                        + cowId));

        // Set cow object
        milkEntry.setCow(cow);

        // Save milk entry
        return milkEntryRepository.save(milkEntry);
    }

    /*
     * Add milk entry using
     * owner name and cow name.
     */
    public MilkEntry addMilkEntry(
            MilkEntryRequestDTO requestDTO) {

        // Fetch cow using owner name
        // and cow name
        Cow cow = cowRepository
                .findByCowNameAndOwnerOwnerName(
                        requestDTO.getCowName(),
                        requestDTO.getOwnerName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cow not found"));

        // Build milk entry object
        MilkEntry milkEntry =
                MilkEntry.builder()
                        .entryDate(
                                requestDTO.getEntryDate())
                        .morningMilk(
                                requestDTO.getMorningMilk())
                        .eveningMilk(
                                requestDTO.getEveningMilk())
                        .pricePerLiter(
                                requestDTO.getPricePerLiter())
                        .cow(cow)
                        .build();

        // Save milk entry
        return milkEntryRepository
                .save(milkEntry);
    }

    /*
     * Update milk entry
     * using owner name.
     */
    public MilkEntry updateMilkEntryByOwner(
            String ownerName,
            MilkEntryRequestDTO requestDTO) {

        // Fetch cow using owner name
        // and cow name
        Cow cow = cowRepository
                .findByCowNameAndOwnerOwnerName(
                        requestDTO.getCowName(),
                        ownerName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cow not found"));

        // Fetch milk entries by cow id
        List<MilkEntry> milkEntries =
                milkEntryRepository
                        .findByCowId(
                                cow.getId());

        // Get first milk entry
        MilkEntry milkEntry =
                milkEntries.stream()
                        .findFirst()
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Milk entry not found"));

        // Update values
        milkEntry.setEntryDate(
                requestDTO.getEntryDate());

        milkEntry.setMorningMilk(
                requestDTO.getMorningMilk());

        milkEntry.setEveningMilk(
                requestDTO.getEveningMilk());

        milkEntry.setPricePerLiter(
                requestDTO.getPricePerLiter());

        // Save updated entry
        return milkEntryRepository
                .save(milkEntry);
    }

    /*
     * Fetch all milk entries.
     */
    public List<MilkEntry> getAllMilkEntries() {

        return milkEntryRepository.findAll();
    }

    /*
     * Fetch milk entries between dates.
     */
    public List<MilkEntry> getMilkReport(
            LocalDate startDate,
            LocalDate endDate) {

        return milkEntryRepository
                .findByEntryDateBetween(
                        startDate,
                        endDate);
    }

    /*
     * Calculate total payment amount.
     */
    public Double calculatePayment(
            LocalDate startDate,
            LocalDate endDate,
            Double pricePerLiter) {

        List<MilkEntry> milkEntries =
                milkEntryRepository
                        .findByEntryDateBetween(
                                startDate,
                                endDate);

        // Java 8 Stream API
        Double totalMilk =
                milkEntries.stream()
                        .mapToDouble(
                                MilkEntry::getTotalMilk)
                        .sum();

        return totalMilk * pricePerLiter;
    }

    /*
     * Fetch milk entries by cow id.
     */
    public List<MilkEntry> getMilkEntriesByCowId(
            Long cowId) {

        return milkEntryRepository
                .findByCowId(cowId);
    }

    /*
     * Fetch all owner wise milk summary report.
     */
    public List<OwnerSummaryDTO>
    getOwnerSummaryReport() {

        List<MilkEntry> milkEntries =
                milkEntryRepository.findAll();

        Map<String, List<MilkEntry>> ownerMap =
                milkEntries.stream()
                        .collect(Collectors.groupingBy(
                                milkEntry ->
                                        milkEntry.getCow()
                                                .getOwner()
                                                .getOwnerName()
                        ));

        return ownerMap.entrySet()
                .stream()
                .map(entry -> {

                    String ownerName =
                            entry.getKey();

                    List<MilkEntry> ownerMilkEntries =
                            entry.getValue();

                    Long totalCows =
                            ownerMilkEntries.stream()
                                    .map(milkEntry ->
                                            milkEntry.getCow()
                                                    .getId())
                                    .distinct()
                                    .count();

                    Double totalMilk =
                            ownerMilkEntries.stream()
                                    .mapToDouble(
                                            MilkEntry::getTotalMilk)
                                    .sum();

                    Double totalAmount =
                            ownerMilkEntries.stream()
                                    .mapToDouble(
                                            MilkEntry::getTotalAmount)
                                    .sum();

                    return OwnerSummaryDTO.builder()
                            .ownerName(ownerName)
                            .totalCows(totalCows)
                            .totalMilkLiters(totalMilk)
                            .totalAmount(totalAmount)
                            .build();
                })
                .toList();
    }

    /*
     * Fetch owner wise summary by owner name.
     */
    public OwnerSummaryDTO
    getOwnerSummaryByName(
            String ownerName) {

        List<MilkEntry> milkEntries =
                milkEntryRepository
                        .findByCowOwnerOwnerName(
                                ownerName);

        Long totalCows =
                milkEntries.stream()
                        .map(milkEntry ->
                                milkEntry.getCow()
                                        .getId())
                        .distinct()
                        .count();

        Double totalMilk =
                milkEntries.stream()
                        .mapToDouble(
                                MilkEntry::getTotalMilk)
                        .sum();

        Double totalAmount =
                milkEntries.stream()
                        .mapToDouble(
                                MilkEntry::getTotalAmount)
                        .sum();

        return OwnerSummaryDTO.builder()
                .ownerName(ownerName)
                .totalCows(totalCows)
                .totalMilkLiters(totalMilk)
                .totalAmount(totalAmount)
                .build();
    }
    /*
     * Fetch milk report
     * by owner name and dates.
     */
    public List<MilkEntry>
    getMilkReportByOwnerName(
            String ownerName,
            LocalDate startDate,
            LocalDate endDate) {

        return milkEntryRepository
                .findByCowOwnerOwnerNameAndEntryDateBetween(
                        ownerName,
                        startDate,
                        endDate);
    }
    /*
     * Fetch monthly milk report
     * by owner name.
     */
    public MonthlyOwnerMilkReportDTO
    getMonthlyMilkReportByOwner(
            String ownerName,
            LocalDate startDate,
            LocalDate endDate) {

        List<MilkEntry> milkEntries =
                milkEntryRepository
                        .findByCowOwnerOwnerNameAndEntryDateBetween(
                                ownerName,
                                startDate,
                                endDate);

        Double totalMilk =
                milkEntries.stream()
                        .mapToDouble(
                                MilkEntry::getTotalMilk)
                        .sum();

        Double totalAmount =
                milkEntries.stream()
                        .mapToDouble(
                                MilkEntry::getTotalAmount)
                        .sum();

        return MonthlyOwnerMilkReportDTO
                .builder()
                .ownerName(ownerName)
                .month(startDate.getMonth().name())
                .year(startDate.getYear())
                .totalMilkLiters(totalMilk)
                .totalAmount(totalAmount)
                .build();
    }
}