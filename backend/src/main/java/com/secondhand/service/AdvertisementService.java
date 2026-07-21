package com.secondhand.service;

import com.secondhand.dto.AdvertisementFilterRequest;
import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.ApiResponse;
import com.secondhand.dto.CreateAdvertisementRequest;
import com.secondhand.dto.UpdateAdvertisementRequest;
import com.secondhand.entity.Advertisement;
import com.secondhand.entity.AdvertisementStatus;
import com.secondhand.entity.User;
import com.secondhand.repository.AdvertisementRepository;
import com.secondhand.specification.AdvertisementSpecification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;

    public AdvertisementService(AdvertisementRepository advertisementRepository, UserService userService) {
        this.advertisementRepository = advertisementRepository;
        this.userService = userService;
    }

    private AdvertisementResponse toResponse(Advertisement ad) {

        AdvertisementResponse response = new AdvertisementResponse(ad.getId(), ad.getTitle(), ad.getDescription(), ad.getPrice(), ad.getCity(), ad.getOwner().getUsername(), ad.getStatus().name(), ad.getViewCount(), ad.getCreatedAt(), ad.getCategory());

        return response;
    }

    public ApiResponse<AdvertisementResponse> create(CreateAdvertisementRequest request, String username) {

        User owner = userService.findByUsername(username);
        if(owner == null) {
            return new ApiResponse<>(false, "You don't have a accont!", null);
        }

        Advertisement ad = new Advertisement();

        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setCity(request.getCity());
        ad.setOwner(owner);
        ad.setCategory(request.getCategory());
        ad.setStatus(AdvertisementStatus.PENDING);

        ad = advertisementRepository.save(ad);

        AdvertisementResponse response = new AdvertisementResponse(ad.getId(), ad.getTitle(), ad.getDescription(), ad.getPrice(), ad.getCity(), owner.getUsername(), ad.getStatus().name(), ad.getViewCount(), ad.getCreatedAt(), ad.getCategory());
        
        return new ApiResponse<>(true, "Ad successfully made", response);
    }

    public ApiResponse<List<AdvertisementResponse>> getAllActive() {

        List<Advertisement> advertisements = advertisementRepository.findByStatus(AdvertisementStatus.ACTIVE);

        List<AdvertisementResponse> response = advertisements.stream().map(this::toResponse).toList();

        return new ApiResponse<>(true, "Active ads list:", response);
    }

    public ApiResponse<AdvertisementResponse> getById(Long id) {

        Advertisement ad = advertisementRepository.findById(id).orElse(null);

        if (ad == null) {
            return new ApiResponse<>(false,"Advertisement not found",null);
        }

        ad.setViewCount(ad.getViewCount() + 1);

        advertisementRepository.save(ad);

        return new ApiResponse<>(true, "Ad has been found and send", toResponse(ad));
    }

    public ApiResponse<List<AdvertisementResponse>> getMyAdvertisements(String username) {
        User owner = userService.findByUsername(username);

        if (owner == null) {
            return new ApiResponse<>(false, "User not found", null);
        }

        List<Advertisement> advertisements = advertisementRepository.findByOwner(owner);
        List<AdvertisementResponse> response = advertisements.stream().map(this::toResponse).toList();

        return new ApiResponse<>(true, "User's ads list:", response);
    }

    public ApiResponse<AdvertisementResponse> update(Long id, UpdateAdvertisementRequest request, String username) {

        Advertisement ad = advertisementRepository.findById(id).orElse(null);

        if (ad == null) {
            return new ApiResponse<>(false,"Advertisement not found",null);
        }
        else if (!ad.getOwner().getUsername().equals(username)) {
            return new ApiResponse<>(false,"User is not the owner of this ad",null);
        }

        if(request.getTitle()!= null) {
            ad.setTitle(request.getTitle());
        }
        if(request.getDescription()!= null) {
            ad.setDescription(request.getDescription());
        }
        if(request.getPrice()!= null) {
            ad.setPrice(request.getPrice());
        }
        if(request.getCity()!= null) {
            ad.setCity(request.getCity());
        }
        if(request.getCategory()!= null) {
            ad.setCategory(request.getCategory());
        }

        if(!(request.getTitle()== null && request.getDescription()== null && request.getPrice()== null && request.getCity()== null && request.getCategory()== null)) {
            ad.setStatus(AdvertisementStatus.PENDING);
        }
        else {
            if(request.getStatus()!= null && (request.getStatus() == AdvertisementStatus.ACTIVE || request.getStatus() == AdvertisementStatus.SOLD)) {
                ad.setStatus(request.getStatus());
            }
        }

        ad = advertisementRepository.save(ad);
        
        return new ApiResponse<>(true, "Ad successfully edited", toResponse(ad));
    }

    public ApiResponse<AdvertisementResponse> delete(Long id, String username) {

        Advertisement ad = advertisementRepository.findById(id).orElse(null);

        if (ad == null) {
            return new ApiResponse<>(false,"Advertisement not found",null);
        }
        else if (!ad.getOwner().getUsername().equals(username)) {
            return new ApiResponse<>(false,"User is not the owner of this ad",null);
        }
        
        advertisementRepository.delete(ad);
        
        return new ApiResponse<>(true, "Ad successfully deleted", null);
    }

    public ApiResponse<AdvertisementResponse> approve(Long id) {

        Advertisement ad = advertisementRepository.findById(id).orElse(null);

        if (ad == null) {
            return new ApiResponse<>(false,"Advertisement not found",null);
        }

        ad.setStatus(AdvertisementStatus.ACTIVE);

        advertisementRepository.save(ad);

        return new ApiResponse<>(true, "Ad successfully approved", toResponse(ad));
    }

    public ApiResponse<AdvertisementResponse> reject(Long id) {

        Advertisement ad = advertisementRepository.findById(id).orElse(null);

        if (ad == null) {
            return new ApiResponse<>(false,"Advertisement not found",null);
        }

        ad.setStatus(AdvertisementStatus.REJECTED);

        advertisementRepository.save(ad);

        return new ApiResponse<>(true, "Ad successfully rejected", toResponse(ad));
    }

    public ApiResponse<List<AdvertisementResponse>> search(AdvertisementFilterRequest request) {

    List<Advertisement> advertisements = advertisementRepository.findAll(AdvertisementSpecification.filter(request));

    List<AdvertisementResponse> response = advertisements.stream().map(this::toResponse).toList();

    return new ApiResponse<>(true, "Search successfully done", response);

    }
}