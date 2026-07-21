package com.secondhand.model;

public class User {

    private Integer id;
    private String username;
    private String password;

    // فیلدهای جدید مخصوص پنل مدیریت
    private String role;
    private String status;

    public User() {
    }

    // سازنده اصلی شما (برای لاگین و ثبت‌نام)
    public User(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // سازنده جدید (برای نمایش کاربران در پنل مدیریت)
    public User(Integer id, String username, String role, String status) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}