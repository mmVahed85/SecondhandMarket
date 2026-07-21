package com.secondhand.repository;

import com.secondhand.entity.AdvertisementImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementImageRepository
        extends JpaRepository<AdvertisementImage, Long> {

                Optional<AdvertisementImage> findById(Long id);

}