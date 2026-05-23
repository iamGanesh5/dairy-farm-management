package com.dairy.farm.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*
 * Entity class representing cow details.
 * This table stores all dairy farm cow information.
 */

@Entity
@Table(name = "cows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
public class Cow {

    /*
     * Primary key for cow table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Name of the cow.
     */
    @Column(name = "cow_name",
            nullable = false,
            length = 100)
    private String cowName;

    /*
     * Breed of the cow.
     */
    @Column(nullable = false,
            length = 50)
    private String breed;

    /*
     * Age of the cow.
     */
    @Column(nullable = false)
    private Integer age;

    /*
     * Unique tag number.
     */
    @Column(name = "tag_number",
            unique = true,
            nullable = false)
    private String tagNumber;

    /*
     * Active status.
     */
    @Column(name = "is_active")
    private Boolean active = true;

    /*
     * Many cows belong to one owner.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id",
            nullable = false)
    private Owner owner;

    /*
     * Record created timestamp.
     */
    @Column(name = "created_at",
            updatable = false)
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

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.active == null) {
            this.active = true;
        }
    }

    /*
     * Executes before updating record.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}