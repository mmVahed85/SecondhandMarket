package com.secondhand.service;

import com.secondhand.config.AppProperties;
import com.secondhand.dto.*;
import com.secondhand.entity.*;
import com.secondhand.service.ImageService;
import com.secondhand.repository.AdvertisementRepository;
import com.secondhand.repository.AdvertisementImageRepository;
import com.secondhand.repository.CommentRepository;
import com.secondhand.specification.AdvertisementSpecification;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementImageRepository advertisementImageRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final AppProperties appProperties;

    public AdvertisementService(AdvertisementRepository advertisementRepository, AdvertisementImageRepository advertisementImageRepository, CommentRepository commentRepository, UserService userService, ImageService imageService, AppProperties appProperties) {
        this.advertisementRepository = advertisementRepository;
        this.userService = userService;
        this.advertisementImageRepository = advertisementImageRepository;
        this.imageService = imageService;
        this.appProperties = appProperties;
        this.commentRepository = commentRepository;
    }

    private AdvertisementResponse toResponse(Advertisement ad) {

        AdvertisementResponse response = new AdvertisementResponse(ad.getId(), ad.getTitle(), ad.getDescription(), ad.getPrice(), ad.getCity(), ad.getOwner().getUsername(), ad.getStatus().name(), ad.getViewCount(), ad.getCreatedAt(), ad.getCategory());
        List<ImageResponse> images = new ArrayList<>();
        for (AdvertisementImage image : ad.getImages()) {
            images.add(new ImageResponse(image.getId(), appProperties.getBaseUrl() + "/uploads/" + image.getImageUrl()));
        }
        response.setImages(images);
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

        Specification<Advertisement> specification = AdvertisementSpecification.filter(request);
        Sort sort = Sort.unsorted();
        if (request.getSortType() != null) {
            switch (request.getSortType()) {
                case NEWEST:
                    sort = Sort.by("createdAt").descending();
                    break;
                case OLDEST:
                    sort = Sort.by("createdAt").ascending();
                    break;
                case PRICE_ASC:
                    sort = Sort.by("price").ascending();
                    break;
                case PRICE_DESC:
                    sort = Sort.by("price").descending();
                    break;
            }
        }
        List<Advertisement> advertisements = advertisementRepository.findAll(specification, sort);
        List<AdvertisementResponse> response = advertisements.stream().map(this::toResponse).toList();
        return new ApiResponse<>(true, "Search successfully done", response);

    }

    public ApiResponse<String> addImage(Long id, MultipartFile file, String username) {

        Advertisement ad = advertisementRepository.findById(id).orElse(null);

        if (ad == null) {
            return new ApiResponse<>(false,"Advertisement not found",null);
        }
        else if (!ad.getOwner().getUsername().equals(username)) {
            return new ApiResponse<>(false,"User is not the owner of this ad",null);
        }

        String fileName = imageService.saveImage(file);
        AdvertisementImage image = new AdvertisementImage();

        image.setAdvertisement(ad);
        image.setImageUrl(fileName);

        advertisementImageRepository.save(image);

        ad.getImages().add(image);

        return new ApiResponse<>(true, "Image uploaded successfully", null);
    }

    public ApiResponse<String> deleteImage(Long imageId, String username) {

        AdvertisementImage image = advertisementImageRepository.findById(imageId).orElse(null);
        if (image == null) {
            return new ApiResponse<>(false,"Image not found",null);
        }
        Advertisement ad = image.getAdvertisement();
        if (!ad.getOwner().getUsername().equals(username)) {
            return new ApiResponse<>(false,"User is not the owner of this ad",null);
        }
        advertisementImageRepository.delete(image);
        return new ApiResponse<>(true, "Image deleted successfully", null);
    }

    public ApiResponse<CommentResponse> addComment(Long advertisementId, CreateCommentRequest request, String username) {

        Advertisement ad = advertisementRepository.findById(advertisementId).orElse(null);

        if (ad == null) {
            return new ApiResponse<>(false,"Advertisement not found",null);
        }
        
        User author = userService.findByUsername(username);
        if(author == null){
            return new ApiResponse<>(false,"User not found",null);
        }
        Comment comment = new Comment(ad, author, request.getText());
        
        ad.getComments().add(comment);
        author.getComments().add(comment);
        commentRepository.save(comment);

        CommentResponse response = new CommentResponse(comment.getId(), comment.getAuthor().getUsername(), comment.getText(), comment.getCreatedAt());
        return new ApiResponse<>(true, "comment saved successfully", response);
    }

    public ApiResponse<List<CommentResponse>> getAllComments(Long advertisementId) {

        Advertisement ad = advertisementRepository.findById(advertisementId).orElse(null);

        if (ad == null) {
            return new ApiResponse<>(false,"Advertisement not found",null);
        }

        List<CommentResponse> responses = new ArrayList<CommentResponse>();
        List<Comment> comments = commentRepository.findByAdvertisementOrderByCreatedAtAsc(ad);

        for(Comment cm : comments) {
            responses.add(new CommentResponse(cm.getId(), cm.getAuthor().getUsername(), cm.getText(), cm.getCreatedAt()));
        }

        return new ApiResponse<>(true, "Advertisment comments list:", responses);
    }
}