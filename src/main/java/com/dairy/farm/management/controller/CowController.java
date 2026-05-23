package com.dairy.farm.management.controller;

import com.dairy.farm.management.entity.Cow;
import com.dairy.farm.management.service.CowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * REST Controller for handling
 * cow related APIs.
 */

@RestController
@RequestMapping("/api/cows")
@RequiredArgsConstructor
public class CowController {

    private final CowService cowService;

    /*
     * API to add new cow.
     */
    @PostMapping
    public Cow addCow(
            @RequestBody Cow cow) {

        return cowService.addCow(cow);
    }

    /*
     * API to fetch all cows.
     */
    @GetMapping
    public List<Cow> getAllCows() {

        return cowService.getAllCows();
    }

    /*
     * API to fetch cow by id.
     */
    @GetMapping("/{cowId}")
    public Cow getCowById(
            @PathVariable Long cowId) {

        return cowService.getCowById(cowId);
    }

    /*
     * API to delete cow by id.
     */
    @DeleteMapping("/{cowId}")
    public String deleteCow(
            @PathVariable Long cowId) {

        cowService.deleteCow(cowId);

        return "Cow deleted successfully";
    }
}