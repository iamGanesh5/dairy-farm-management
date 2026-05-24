package com.dairy.farm.management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryDTO {

    private Long totalOwners;

    private Long totalCows;

    private Double totalMilk;

    private Double totalRevenue;
}