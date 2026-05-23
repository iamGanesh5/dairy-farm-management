package com.dairy.farm.management.dto;

import lombok.*;

import java.time.LocalDate;

/*
 * DTO for adding milk entry
 * using owner name and cow name.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilkEntryRequestDTO {

    private String ownerName;

    private String cowName;

    private LocalDate entryDate;

    private Double morningMilk;

    private Double eveningMilk;

    private Double pricePerLiter;
}