package com.secondhand.controller;

import com.secondhand.dto.UpdateProfileRequest;
import com.secondhand.dto.UserResponse;
import com.secondhand.service.UserService;
import com.secondhand.util.ApiResponse;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getProfile(
            Authentication authentication) {

        return userService.getProfile(
                authentication.getName()
        );
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateProfile(

            Authentication authentication,

            @Valid
            @RequestBody
            UpdateProfileRequest request) {

        return userService.updateProfile(
                authentication.getName(),
                request
        );
    }

}