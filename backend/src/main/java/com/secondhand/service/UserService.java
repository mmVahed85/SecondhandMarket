package com.secondhand.service;

import com.secondhand.dto.UserResponse;
import com.secondhand.entity.User;
import com.secondhand.repository.UserRepository;
import com.secondhand.util.ApiResponse;

import java.util.ArrayList;
import java.util.List;

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

    public ApiResponse<String> blockUser(Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ApiResponse<>(false,
                    "User not found",
                    null);
        }

        user.setEnabled(false);

        userRepository.save(user);

        return new ApiResponse<>(
                true,
                "User blocked successfully",
                null
        );

    }

    public ApiResponse<String> unblockUser(Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ApiResponse<>(false,
                    "User not found",
                    null);
        }

        user.setEnabled(true);

        userRepository.save(user);

        return new ApiResponse<>(
                true,
                "User unblocked successfully",
                null
        );

    }

    public ApiResponse<List<UserResponse>> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserResponse> response = new ArrayList<>();

        for (User user : users) {

            response.add(new UserResponse(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.isEnabled(), user.getRole()));

        }

        return new ApiResponse<>(
                true,
                "Users loaded successfully",
                response
        );

    }
}