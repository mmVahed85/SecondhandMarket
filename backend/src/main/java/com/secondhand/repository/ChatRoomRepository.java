package com.secondhand.repository;

import com.secondhand.entity.Advertisement;
import com.secondhand.entity.ChatRoom;
import com.secondhand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByAdvertisementAndBuyerAndSeller(
            Advertisement advertisement,
            User buyer,
            User seller
    );

    List<ChatRoom> findByBuyer(User buyer);

    List<ChatRoom> findBySeller(User seller);

    List<ChatRoom> findByBuyerOrSeller(
            User buyer,
            User seller
    );

}