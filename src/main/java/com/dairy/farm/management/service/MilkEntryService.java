package com.dairy.farm.management.service;

import com.dairy.farm.management.dto.DashboardSummaryDTO;
import com.dairy.farm.management.dto.MilkEntryRequestDTO;
import com.dairy.farm.management.dto.MonthlyOwnerMilkReportDTO;
import com.dairy.farm.management.dto.OwnerSummaryDTO;
import com.dairy.farm.management.entity.Cow;
import com.dairy.farm.management.entity.MilkEntry;
import com.dairy.farm.management.exception.ResourceNotFoundException;
import com.dairy.farm.management.repository.CowRepository;
import com.dairy.farm.management.repository.MilkEntryRepository;
import com.dairy.farm.management.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.dairy.farm.management.dto.OwnerDashboardSummaryDTO;
import com.dairy.farm.management.entity.Owner;

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

    private final OwnerRepository ownerRepository;

    /*
     * Add daily milk entry
     * using cow id.
     */
    public MilkEntry addMilkEntry(
            Long cowId,
            MilkEntry milkEntry) {

        Cow cow = cowRepository.findById(cowId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cow not found with id : "
                                        + cowId));

        milkEntry.setCow(cow);

        return milkEntryRepository.save(milkEntry);
    }

    /*
     * Add milk entry using
     * owner name and cow name.
     */
    public MilkEntry addMilkEntry(
            MilkEntryRequestDTO requestDTO) {

        Cow cow = cowRepository
                .findByCowNameAndOwnerOwnerName(
                        requestDTO.getCowName(),
                        requestDTO.getOwnerName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cow not found"));

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

        return milkEntryRepository.save(milkEntry);
    }

    /*
     * Update milk entry
     * using owner name.
     */
    public MilkEntry updateMilkEntryByOwner(
            String ownerName,
            MilkEntryRequestDTO requestDTO) {

        Cow cow = cowRepository
                .findByCowNameAndOwnerOwnerName(
                        requestDTO.getCowName(),
                        ownerName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cow not found"));

        List<MilkEntry> milkEntries =
                milkEntryRepository.findByCowId(
                        cow.getId());

        MilkEntry milkEntry =
                milkEntries.stream()
                        .findFirst()
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Milk entry not found"));

        milkEntry.setEntryDate(
                requestDTO.getEntryDate());

        milkEntry.setMorningMilk(
                requestDTO.getMorningMilk());

        milkEntry.setEveningMilk(
                requestDTO.getEveningMilk());

        milkEntry.setPricePerLiter(
                requestDTO.getPricePerLiter());

        return milkEntryRepository.save(milkEntry);
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

    /*
     * Fetch dashboard summary.
     */
    public DashboardSummaryDTO
    getDashboardSummary() {

        Long totalOwners =
                ownerRepository.count();

        Long totalCows =
                cowRepository.count();

        List<MilkEntry> milkEntries =
                milkEntryRepository.findAll();

        Double totalMilk =
                milkEntries.stream()
                        .mapToDouble(
                                MilkEntry::getTotalMilk)
                        .sum();

        Double totalRevenue =
                milkEntries.stream()
                        .mapToDouble(
                                MilkEntry::getTotalAmount)
                        .sum();

        return DashboardSummaryDTO
                .builder()
                .totalOwners(totalOwners)
                .totalCows(totalCows)
                .totalMilk(totalMilk)
                .totalRevenue(totalRevenue)
                .build();
    }

    /*
     * Fetch owner dashboard summary.
     */
    public OwnerDashboardSummaryDTO
    getOwnerDashboardSummary(
            Long ownerId
    ) {

        Owner owner = ownerRepository
                .findById(ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Owner not found"
                        ));

        Long totalCows =
                cowRepository.countByOwnerId(
                        ownerId
                );

        Double todayMilk =
                milkEntryRepository.getTodayMilk(
                        ownerId,
                        LocalDate.now()
                );

        Double monthlyMilk =
                milkEntryRepository.getMonthlyMilk(
                        ownerId,
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear()
                );

        Double yearlyMilk =
                milkEntryRepository.getYearlyMilk(
                        ownerId,
                        LocalDate.now().getYear()
                );

        Double monthlyRevenue =
                milkEntryRepository.getMonthlyRevenue(
                        ownerId,
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear()
                );

        Double yearlyRevenue =
                milkEntryRepository.getYearlyRevenue(
                        ownerId,
                        LocalDate.now().getYear()
                );

        return OwnerDashboardSummaryDTO
                .builder()
                .ownerName(owner.getOwnerName())
                .totalCows(totalCows)
                .todayMilk(todayMilk)
                .monthlyMilk(monthlyMilk)
                .yearlyMilk(yearlyMilk)
                .monthlyRevenue(monthlyRevenue)
                .yearlyRevenue(yearlyRevenue)
                .build();

    }
}