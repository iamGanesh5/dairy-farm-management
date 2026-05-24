package com.dairy.farm.management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerDashboardSummaryDTO {

    private String ownerName;

    private Long totalCows;

    private Double todayMilk;

    private Double monthlyMilk;

    private Double yearlyMilk;

    private Double monthlyRevenue;

    private Double yearlyRevenue;

}