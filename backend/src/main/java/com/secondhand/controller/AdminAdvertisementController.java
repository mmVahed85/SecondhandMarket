package com.secondhand.controller;

import com.secondhand.dto.*;
import com.secondhand.service.AdvertisementService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

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

}