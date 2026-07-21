package com.secondhand.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Advertisement advertisement;

    @ManyToOne
    private User author;

    private String text;

    private LocalDateTime createdAt;
}