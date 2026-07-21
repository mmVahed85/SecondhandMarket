package com.secondhand.repository;

import com.secondhand.entity.Comment;
import com.secondhand.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long> {

    List<Comment> findByAdvertisementOrderByCreatedAtAsc(Advertisement advertisement);


}