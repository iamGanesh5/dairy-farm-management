package com.dairy.farm.management.dto;

import lombok.*;

/*
 * DTO for owner milk summary report.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerSummaryDTO {

    private String ownerName;

    private Long totalCows;

    private Double totalMilkLiters;

    private Double totalAmount;
}