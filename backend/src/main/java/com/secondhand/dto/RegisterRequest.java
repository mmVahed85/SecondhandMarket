package com.secondhand.dto;

import javax.validation.constraints.*;

public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must be less than 50 characters")
    private String username;


    @NotBlank(message = "Password is required")
    private String password;


    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;


    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;


    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;


    @NotBlank(message = "Phone is required")
    @Size(max = 20)
    private String phone;

    public RegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}