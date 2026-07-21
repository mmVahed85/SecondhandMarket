package com.secondhand.controller;

import com.secondhand.dto.*;
import com.secondhand.service.AdvertisementService;
import com.secondhand.util.ApiResponse;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/advertisements")
public class AdminAdvertisementController {

    private final AdvertisementService advertisementService;

    public AdminAdvertisementController(
            AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PatchMapping("/{id}/approve")
    public ApiResponse<AdvertisementResponse> approve(@PathVariable Long id) {

        return advertisementService.approve(id);
    }

    @PatchMapping("/{id}/reject")
    public ApiResponse<AdvertisementResponse> reject(@PathVariable Long id) {

        return advertisementService.reject(id);
    }

    @GetMapping("/pending")
    public ApiResponse<List<AdvertisementResponse>>
    getPendingAdvertisements() {

        return advertisementService.getPendingAdvertisements();

    }

}