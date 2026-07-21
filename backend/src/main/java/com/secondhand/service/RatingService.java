package com.secondhand.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.secondhand.dto.ApiResponse;
import com.secondhand.dto.RatingRequest;
import com.secondhand.dto.RatingResponse;
import com.secondhand.entity.Advertisement;
import com.secondhand.entity.Rating;
import com.secondhand.entity.User;
import com.secondhand.repository.AdvertisementRepository;
import com.secondhand.repository.RatingRepository;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;

    public RatingService(
            RatingRepository ratingRepository,
            AdvertisementRepository advertisementRepository,
            UserService userService) {

        this.ratingRepository = ratingRepository;
        this.advertisementRepository = advertisementRepository;
        this.userService = userService;
    }

    public ApiResponse<RatingResponse> rate(
            Long advertisementId,
            RatingRequest request,
            String username) {

        Advertisement advertisement =
                advertisementRepository.findById(advertisementId)
                        .orElse(null);

        if (advertisement == null)
            return new ApiResponse<>(false,
                    "Advertisement not found",
                    null);

        User user = userService.findByUsername(username);

        if (user == null)
            return new ApiResponse<>(false,
                    "User not found",
                    null);

        Rating rating =
                ratingRepository
                        .findByAdvertisementAndUser(
                                advertisement,
                                user
                        )
                        .orElse(null);

        if (rating == null) {

            rating = new Rating(
                    advertisement,
                    user,
                    request.getScore()
            );

        } else {

            rating.setScore(request.getScore());

        }

        ratingRepository.save(rating);

        List<Rating> ratings = advertisement.getRatings();

        double average = 0;

        for (Rating r : ratings) {
            average += r.getScore();
        }

        if (!ratings.isEmpty()) {
            average /= ratings.size();
        }

        Integer count =
                ratingRepository
                        .countByAdvertisement(advertisement);

        RatingResponse response =
                new RatingResponse(
                        average,
                        count,
                        rating.getScore()
                );

        return new ApiResponse<>(
                true,
                "Rating saved successfully",
                response
        );

    }
}