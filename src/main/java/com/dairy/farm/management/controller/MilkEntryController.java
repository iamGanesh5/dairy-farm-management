package com.dairy.farm.management.controller;

import com.dairy.farm.management.dto.*;
import com.dairy.farm.management.entity.MilkEntry;
import com.dairy.farm.management.service.MilkEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/*
 * REST Controller for handling
 * milk entry APIs.
 */

@RestController
@RequestMapping("/api/milk-entries")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MilkEntryController {

    private final MilkEntryService milkEntryService;

    /*
     * API to add daily milk entry
     * using cow id.
     */
    @PostMapping("/{cowId}")
    public MilkEntry addMilkEntry(
            @PathVariable Long cowId,
            @RequestBody MilkEntry milkEntry) {

        return milkEntryService
                .addMilkEntry(cowId, milkEntry);
    }

    /*
     * API to add milk entry
     * using owner name and cow name.
     */
    @PostMapping
    public MilkEntry addMilkEntry(
            @RequestBody
            MilkEntryRequestDTO requestDTO) {

        return milkEntryService
                .addMilkEntry(requestDTO);
    }

    /*
     * API to fetch all milk entries.
     */
    @GetMapping
    public List<MilkEntry> getAllMilkEntries() {

        return milkEntryService.getAllMilkEntries();
    }

    /*
     * API to fetch milk report
     * between dates.
     */
    @GetMapping("/report")
    public List<MilkEntry> getMilkReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return milkEntryService
                .getMilkReport(startDate, endDate);
    }

    /*
     * API to calculate payment.
     */
    @GetMapping("/payment")
    public Double calculatePayment(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam Double pricePerLiter) {

        return milkEntryService.calculatePayment(
                startDate,
                endDate,
                pricePerLiter);
    }

    /*
     * API to fetch milk entries by cow id.
     */
    @GetMapping("/cow/{cowId}")
    public List<MilkEntry> getMilkEntriesByCowId(
            @PathVariable Long cowId) {

        return milkEntryService
                .getMilkEntriesByCowId(cowId);
    }

    /*
     * API to fetch owner wise summary.
     */
    @GetMapping("/owner-summary")
    public List<OwnerSummaryDTO>
    getOwnerSummaryReport() {

        return milkEntryService
                .getOwnerSummaryReport();
    }

    /*
     * API to fetch summary by owner name.
     */
    @GetMapping("/owner-summary/{ownerName}")
    public OwnerSummaryDTO
    getOwnerSummaryByName(
            @PathVariable String ownerName) {

        return milkEntryService
                .getOwnerSummaryByName(
                        ownerName);
    }

    /*
     * API to update milk entry
     * using owner name.
     */
    @PutMapping("/owner/{ownerName}")
    public MilkEntry updateMilkEntryByOwner(
            @PathVariable String ownerName,
            @RequestBody MilkEntryRequestDTO requestDTO) {

        return milkEntryService
                .updateMilkEntryByOwner(
                        ownerName,
                        requestDTO);
    }
    /*
     * API to fetch milk report
     * by owner name and dates.
     */
    @GetMapping("/report/{ownerName}")
    public List<MilkEntry>
    getMilkReportByOwnerName(
            @PathVariable String ownerName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return milkEntryService
                .getMilkReportByOwnerName(
                        ownerName,
                        startDate,
                        endDate);
    }
    /*
     * API to fetch monthly milk report
     * by owner name.
     */
    @GetMapping("/monthly-report/{ownerName}")
    public MonthlyOwnerMilkReportDTO
    getMonthlyMilkReportByOwner(
            @PathVariable String ownerName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return milkEntryService
                .getMonthlyMilkReportByOwner(
                        ownerName,
                        startDate,
                        endDate);
    }
    @GetMapping("/dashboard-summary")
    public DashboardSummaryDTO
    getDashboardSummary() {

        return milkEntryService
                .getDashboardSummary();
    }


    @GetMapping(
            "/owner-dashboard/{ownerId}"
    )
    public OwnerDashboardSummaryDTO
    getOwnerDashboardSummary(

            @PathVariable
            Long ownerId

    ) {

        return milkEntryService
                .getOwnerDashboardSummary(
                        ownerId
                );

    }
}