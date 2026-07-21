package com.secondhand.repository;

import com.secondhand.entity.ChatRoom;
import com.secondhand.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatRoomOrderByCreatedAtAsc(
            ChatRoom chatRoom
    );

}