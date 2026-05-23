package com.dairy.farm.management.scheduler;

import com.dairy.farm.management.entity.MilkEntry;
import com.dairy.farm.management.repository.MilkEntryRepository;
import com.dairy.farm.management.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MilkReportScheduler {

    private final MilkEntryRepository milkEntryRepository;
    private final NotificationService notificationService;

    /*
     * Runs every day at 8 PM.
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void sendDailyMilkReport() {

        List<MilkEntry> milkEntries =
                milkEntryRepository
                        .findByEntryDate(
                                LocalDate.now());

        for (MilkEntry milkEntry : milkEntries) {

            String message =
                    "Hello "
                            + milkEntry.getCow()
                            .getOwner()
                            .getOwnerName()
                            + "\n\n"
                            + "Morning Milk : "
                            + milkEntry.getMorningMilk()
                            + " Liters\n"
                            + "Evening Milk : "
                            + milkEntry.getEveningMilk()
                            + " Liters\n"
                            + "Total Milk : "
                            + milkEntry.getTotalMilk()
                            + " Liters\n"
                            + "Amount : ₹"
                            + milkEntry.getTotalAmount();

            notificationService
                    .sendDailyMilkMessage(
                            milkEntry.getCow()
                                    .getOwner()
                                    .getMobileNumber(),
                            message);
        }
    }
}