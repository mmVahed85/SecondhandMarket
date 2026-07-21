package com.secondhand.service;

import com.secondhand.entity.User;
import com.secondhand.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->new RuntimeException("User not found"));
    }

    public User findByUsername(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {throw new RuntimeException("User not found");}

        return user;
    }

}