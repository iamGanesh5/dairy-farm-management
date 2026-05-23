package com.dairy.farm.management.repository;

import com.dairy.farm.management.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository
        extends JpaRepository<Owner, Long> {
}