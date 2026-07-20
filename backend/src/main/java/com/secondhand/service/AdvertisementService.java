package com.secondhand.service;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.CreateAdvertisementRequest;
import com.secondhand.entity.Advertisement;
import com.secondhand.entity.AdvertisementStatus;
import com.secondhand.entity.User;
import com.secondhand.repository.AdvertisementRepository;
import com.secondhand.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;

    public AdvertisementService(AdvertisementRepository advertisementRepository, UserService userService) {
        this.advertisementRepository = advertisementRepository;
        this.userService = userService;
    }

    public AdvertisementResponse create(
            CreateAdvertisementRequest request,
            String username) {

        User owner = userService.findByUsername(username);

        if (owner == null)
            throw new RuntimeException("User not found");

        Advertisement ad = new Advertisement();

        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setCity(request.getCity());
        ad.setOwner(owner);
        ad.setStatus(AdvertisementStatus.PENDING);

        advertisementRepository.save(ad);

        AdvertisementResponse response = new AdvertisementResponse();

        response.setId(ad.getId());
        response.setTitle(ad.getTitle());
        response.setDescription(ad.getDescription());
        response.setPrice(ad.getPrice());
        response.setCity(ad.getCity());

        return response;
    }

}