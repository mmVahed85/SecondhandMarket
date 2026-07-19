package com.secondhand.service;

import com.secondhand.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getUser() {
        return new User(1, "admin", "123456");
    }

}