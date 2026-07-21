package com.secondhand.repository;

import com.secondhand.entity.Advertisement;
import com.secondhand.entity.Rating;
import com.secondhand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository
        extends JpaRepository<Rating, Long> {

    Optional<Rating> findByAdvertisementAndUser(
            Advertisement advertisement,
            User user
    );

    Double getAverageScoreByAdvertisement(Advertisement advertisement);

    Integer countByAdvertisement(Advertisement advertisement);

    List<Rating> findByAdvertisement(Advertisement advertisement);

}