package com.dairy.farm.management.service;

import com.dairy.farm.management.entity.Cow;
import com.dairy.farm.management.entity.Owner;
import com.dairy.farm.management.exception.ResourceNotFoundException;
import com.dairy.farm.management.repository.CowRepository;
import com.dairy.farm.management.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service class for handling
 * cow related business logic.
 */

@Service
@RequiredArgsConstructor
public class CowService {

    private final CowRepository cowRepository;
    private final OwnerRepository ownerRepository;

    /*
     * Add new cow details.
     */
    public Cow addCow(Cow cow) {

        // Fetch owner from DB
        Owner owner = ownerRepository.findById(
                        cow.getOwner().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Owner not found"));

        // Set complete owner object
        cow.setOwner(owner);

        return cowRepository.save(cow);
    }

    /*
     * Fetch all cows.
     */
    public List<Cow> getAllCows() {

        return cowRepository.findAll();
    }

    /*
     * Fetch cow by id.
     */
    public Cow getCowById(Long cowId) {

        return cowRepository.findById(cowId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cow not found with id : "
                                        + cowId));
    }

    /*
     * Delete cow by id.
     */
    public void deleteCow(Long cowId) {

        // Validate cow existence
        Cow cow = cowRepository.findById(cowId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cow not found with id : "
                                        + cowId));

        cowRepository.delete(cow);
    }
}