package com.dairy.farm.management.controller;

import com.dairy.farm.management.entity.Owner;
import com.dairy.farm.management.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    /*
     * Add owner API.
     */
    @PostMapping
    public Owner addOwner(
            @RequestBody Owner owner) {

        return ownerService.addOwner(owner);
    }

    /*
     * Fetch all owners.
     */
    @GetMapping
    public List<Owner> getAllOwners() {

        return ownerService.getAllOwners();
    }
}