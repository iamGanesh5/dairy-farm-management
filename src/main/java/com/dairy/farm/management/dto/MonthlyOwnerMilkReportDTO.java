package com.dairy.farm.management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyOwnerMilkReportDTO {

    private String ownerName;

    private String month;

    private Integer year;

    private Double totalMilkLiters;

    private Double totalAmount;
}