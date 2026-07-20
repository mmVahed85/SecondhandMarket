package com.secondhand.util;

public class SessionManager {

    private static String token;

    // متد برای ذخیره توکن
    public static void setToken(String jwtToken) {
        token = jwtToken;
    }

    // متد برای دریافت توکن
    public static String getToken() {
        return token;
    }

    // بررسی اینکه آیا کاربر لاگین کرده است یا خیر
    public static boolean isLoggedIn() {
        return token != null && !token.trim().isEmpty();
    }

    // خروج از حساب کاربری (پاک کردن توکن)
    public static void logout() {
        token = null;
    }
}