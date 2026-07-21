package com.secondhand.repository;

import com.secondhand.entity.Advertisement;
import com.secondhand.entity.AdvertisementStatus;
import com.secondhand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, JpaSpecificationExecutor<Advertisement> {

    List<Advertisement> findByOwner(User owner);

    List<Advertisement> findByStatus(AdvertisementStatus status);

    List<Advertisement> findByStatusOrderByCreatedAtAsc(AdvertisementStatus status);

}