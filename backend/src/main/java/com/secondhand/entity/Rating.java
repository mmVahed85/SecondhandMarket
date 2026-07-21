package com.secondhand.entity;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"advertisement_id", "user_id"}))
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Advertisement advertisement;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private Integer score;

    public Rating() {
    }

    public Rating(Advertisement advertisement, User user, Integer score) {
        this.advertisement = advertisement;
        this.user = user;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}