package com.secondhand.controller;

import com.secondhand.dto.*;
import com.secondhand.service.AdvertisementService;
import com.secondhand.service.RatingService;

import java.util.List;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;



@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final RatingService ratingService;

    public AdvertisementController(AdvertisementService advertisementService, RatingService ratingService) {
        this.advertisementService = advertisementService;
        this.ratingService = ratingService;
    }

    @PostMapping
    public ApiResponse<AdvertisementResponse> create(@RequestBody @Valid CreateAdvertisementRequest request, Authentication authentication) {

        return advertisementService.create(request,authentication.getName());
    }

    @GetMapping
    public ApiResponse<List<AdvertisementResponse>> getAllActive() {

        return advertisementService.getAllActive();

    }

    @GetMapping("/{id}")
    public ApiResponse<AdvertisementResponse> getById(@PathVariable Long id) {
        return advertisementService.getById(id);
    }

    @GetMapping("/my")
    public ApiResponse<List<AdvertisementResponse>> myAdvertisements(Authentication authentication) {

        return advertisementService.getMyAdvertisements(authentication.getName());
    }

    @PutMapping("/{id}")
    public ApiResponse<AdvertisementResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateAdvertisementRequest request, Authentication authentication) {

        return advertisementService.update(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<AdvertisementResponse> delete(@PathVariable Long id, Authentication authentication) {

        return advertisementService.delete(id, authentication.getName());
    }

    @PostMapping("/search")
    public ApiResponse<List<AdvertisementResponse>> search(@RequestBody AdvertisementFilterRequest request) {

        return advertisementService.search(request);
    }

    @PostMapping("/{id}/images")
    public ApiResponse<String> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile image, Authentication authentication) {

        return advertisementService.addImage(id, image, authentication.getName());
    }

    @DeleteMapping("/images/{imageId}")
    public ApiResponse<String> deleteImage(@PathVariable Long imageId,Authentication authentication) {

        return advertisementService.deleteImage(imageId,authentication.getName());
    }

    @PostMapping("/{advertisementId}/comments")
    public ApiResponse<CommentResponse> createComment(@PathVariable Long advertisementId, @Valid @RequestBody CreateCommentRequest request, Authentication authentication) {

        return advertisementService.addComment(advertisementId, request, authentication.getName());
    }


    @GetMapping("/{advertisementId}/comments")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long advertisementId) {

        return advertisementService.getAllComments(advertisementId);
    }

    @PostMapping("/{advertisementId}/rating")
    public ApiResponse<RatingResponse> rateAdvertisement(@PathVariable Long advertisementId, @Valid @RequestBody RatingRequest request, Authentication authentication) {

        return ratingService.rate(advertisementId, request, authentication.getName()

        );

    }

    @PostMapping("/{advertisementId}/favorite")
    public ApiResponse<String> addFavorite(
            @PathVariable Long advertisementId,
            Authentication authentication) {

        return advertisementService.addFavorite(
                advertisementId,
                authentication.getName()
        );

    }

    @DeleteMapping("/{advertisementId}/favorite")
    public ApiResponse<String> removeFavorite(
            @PathVariable Long advertisementId,
            Authentication authentication) {

        return advertisementService.removeFavorite(
                advertisementId,
                authentication.getName()
        );

    }

    @GetMapping("/me/favorites")
    public ApiResponse<List<AdvertisementResponse>>
    getFavorites(Authentication authentication) {

        return advertisementService.getFavorites(
                authentication.getName()
        );

    }
}