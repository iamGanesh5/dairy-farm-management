package com.dairy.farm.management.service;

import com.dairy.farm.management.entity.Owner;
import com.dairy.farm.management.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    /*
     * Add owner.
     */
    public Owner addOwner(Owner owner) {

        return ownerRepository.save(owner);
    }

    /*
     * Fetch all owners.
     */
    public List<Owner> getAllOwners() {

        return ownerRepository.findAll();
    }
}