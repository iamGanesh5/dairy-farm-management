package com.dairy.farm.management.controller;

import com.dairy.farm.management.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /*
     * API to generate monthly PDF report.
     */
    @GetMapping("/monthly/{ownerName}")
    public ResponseEntity<byte[]> generateMonthlyReport(
            @PathVariable String ownerName,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        ByteArrayInputStream pdfReport =
                reportService.generateMonthlyPdfReport(
                        ownerName,
                        startDate,
                        endDate);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=milk_report.pdf")
                .contentType(
                        MediaType.APPLICATION_PDF)
                .body(pdfReport.readAllBytes());
    }
}