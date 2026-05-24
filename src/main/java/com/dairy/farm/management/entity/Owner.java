package com.dairy.farm.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Entity representing owner details.
 */

@Entity
@Table(name = "owners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Owner name.
     */
    @Column(
            name = "owner_name",
            nullable = false
    )
    private String ownerName;

    /*
     * Mobile number.
     */
    @Column(name = "mobile_number")
    private String mobileNumber;

    /*
     * Owner address.
     */
    @Column(name = "address")
    private String address;

    /*
     * One owner can have multiple cows.
     */
    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Cow> cows;

    @Column(
            name = "created_at",
            updatable = false
    )
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

    }
}