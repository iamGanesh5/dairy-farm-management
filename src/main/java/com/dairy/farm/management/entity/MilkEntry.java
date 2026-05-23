package com.dairy.farm.management.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;

/*
 * Entity class representing daily milk collection details.
 * Stores morning, evening and total milk production data.
 */

@Entity
@Table(name = "milk_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
public class MilkEntry {

    /*
     * Primary key for milk entry table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Milk entry date.
     */
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    /*
     * Morning milk quantity in liters.
     */
    @Column(name = "morning_milk", nullable = false)
    private Double morningMilk;

    /*
     * Evening milk quantity in liters.
     */
    @Column(name = "evening_milk", nullable = false)
    private Double eveningMilk;

    /*
     * Total milk quantity.
     * Automatically calculated.
     */
    @Column(name = "total_milk", nullable = false)
    private Double totalMilk;

    /*
     * Price per liter provided by milk agent.
     * Example: 30 Rs per liter.
     */
    @Column(name = "price_per_liter")
    private Double pricePerLiter;

    /*
     * Total payment amount.
     * Formula:
     * totalMilk * pricePerLiter
     */
    @Column(name = "total_amount")
    private Double totalAmount;

    /*
     * Many milk entries belong to one cow.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cow_id", nullable = false)
    private Cow cow;

    /*
     * Record created timestamp.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /*
     * Record updated timestamp.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
     * Executes before inserting record.
     */
    @PrePersist
    public void prePersist() {

        calculateMilkAndAmount();

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /*
     * Executes before updating record.
     */
    @PreUpdate
    public void preUpdate() {

        calculateMilkAndAmount();

        this.updatedAt = LocalDateTime.now();
    }

    /*
     * Common method for calculating
     * total milk and payment amount.
     */
    private void calculateMilkAndAmount() {

        // Null safety check
        double morning = morningMilk != null ? morningMilk : 0;
        double evening = eveningMilk != null ? eveningMilk : 0;

        // Calculate total milk
        this.totalMilk = morning + evening;

        // Calculate total payment amount
        if (this.pricePerLiter != null) {
            this.totalAmount =
                    this.totalMilk * this.pricePerLiter;
        }
    }
}