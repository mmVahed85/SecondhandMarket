package com.secondhand.controller;

import com.secondhand.dto.UserResponse;
import com.secondhand.service.UserService;
import com.secondhand.util.ApiResponse;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {

        this.userService = userService;
    }

    @PutMapping("/{userId}/block")
    public ApiResponse<String> blockUser(
            @PathVariable Long userId) {

        return userService.blockUser(userId);
    }

    @PutMapping("/{userId}/unblock")
    public ApiResponse<String> unblockUser(
            @PathVariable Long userId) {

        return userService.unblockUser(userId);
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(
            @PathVariable Long userId) {

        return userService.unblockUser(userId);
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {

        return userService.getAllUsers();

    }
}