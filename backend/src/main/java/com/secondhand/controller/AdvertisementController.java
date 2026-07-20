package com.secondhand.controller;

import com.secondhand.dto.*;
import com.secondhand.service.AdvertisementService;

import java.util.List;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostMapping
    public ApiResponse<AdvertisementResponse> create(@RequestBody @Valid CreateAdvertisementRequest request, Authentication authentication) {

        return advertisementService.create(request,authentication.getName());
    }

    @GetMapping
    public ApiResponse<List<AdvertisementResponse>> getAllActive() {

        return advertisementService.getAllActive();

    }
    
}