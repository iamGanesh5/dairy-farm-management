package com.dairy.farm.management.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    /*
     * Send daily milk message.
     */
    public void sendDailyMilkMessage(
            String mobileNumber,
            String message) {

        // Twilio / WhatsApp API integration

        System.out.println(
                "Message Sent To : "
                        + mobileNumber);

        System.out.println(message);
    }
}