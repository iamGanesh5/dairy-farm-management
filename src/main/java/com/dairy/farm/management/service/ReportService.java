package com.dairy.farm.management.service;

import com.dairy.farm.management.entity.MilkEntry;
import com.dairy.farm.management.repository.MilkEntryRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MilkEntryRepository milkEntryRepository;

    /*
     * Generate monthly PDF report.
     */
    public ByteArrayInputStream generateMonthlyPdfReport(
            String ownerName,
            LocalDate startDate,
            LocalDate endDate) {

        List<MilkEntry> milkEntries =
                milkEntryRepository
                        .findByCowOwnerOwnerNameAndEntryDateBetween(
                                ownerName,
                                startDate,
                                endDate);

        Document document = new Document();

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            Font font =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            18);

            Paragraph title =
                    new Paragraph(
                            "DAIRY FARM MONTHLY REPORT",
                            font);

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Owner Name : " + ownerName));

            document.add(new Paragraph(
                    "Start Date : " + startDate));

            document.add(new Paragraph(
                    "End Date : " + endDate));

            document.add(new Paragraph(" "));

            double totalMilk = 0;
            double totalAmount = 0;

            for (MilkEntry milkEntry : milkEntries) {

                document.add(new Paragraph(
                        "Date : "
                                + milkEntry.getEntryDate()));

                document.add(new Paragraph(
                        "Cow Name : "
                                + milkEntry.getCow()
                                .getCowName()));

                document.add(new Paragraph(
                        "Morning Milk : "
                                + milkEntry.getMorningMilk()));

                document.add(new Paragraph(
                        "Evening Milk : "
                                + milkEntry.getEveningMilk()));

                document.add(new Paragraph(
                        "Total Milk : "
                                + milkEntry.getTotalMilk()));

                document.add(new Paragraph(
                        "Total Amount : ₹"
                                + milkEntry.getTotalAmount()));

                document.add(new Paragraph(
                        "--------------------------------"));

                totalMilk += milkEntry.getTotalMilk();

                totalAmount += milkEntry.getTotalAmount();
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph(
                    "TOTAL MILK : "
                            + totalMilk + " Liters"));

            document.add(new Paragraph(
                    "TOTAL AMOUNT : ₹"
                            + totalAmount));

            document.close();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error while generating PDF");
        }

        return new ByteArrayInputStream(
                out.toByteArray());
    }
}