package com.secondhand.service;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.ApiResponse;
import com.secondhand.dto.CreateAdvertisementRequest;
import com.secondhand.entity.Advertisement;
import com.secondhand.entity.AdvertisementStatus;
import com.secondhand.entity.User;
import com.secondhand.repository.AdvertisementRepository;

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
        ad.setStatus(AdvertisementStatus.PENDING);

        ad = advertisementRepository.save(ad);

        AdvertisementResponse response = new AdvertisementResponse(ad.getId(), ad.getTitle(), ad.getDescription(), ad.getPrice(), ad.getCity(), owner.getUsername(), ad.getStatus().name(), ad.getViewCount(), ad.getCreatedAt());
        
        return new ApiResponse<>(true, "Ad successfully made", response);
    }

    public ApiResponse<List<AdvertisementResponse>> getAllActive() {

        List<Advertisement> advertisements = advertisementRepository.findByStatus(AdvertisementStatus.ACTIVE);

        List<AdvertisementResponse> response = new ArrayList<>();

        for (Advertisement ad : advertisements) {

            AdvertisementResponse dto = new AdvertisementResponse(ad.getId(), ad.getTitle(), ad.getDescription(), ad.getPrice(), ad.getCity(), ad.getOwner().getUsername(), ad.getStatus().name(), ad.getViewCount(), ad.getCreatedAt());

            response.add(dto);
        }

        return new ApiResponse<>(true, "Active ads list:", response);
    }

}