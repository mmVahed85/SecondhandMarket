package com.secondhand.repository;

import com.secondhand.entity.Advertisement;
import com.secondhand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement,Long> {

    List<Advertisement> findByOwner(User owner);

}