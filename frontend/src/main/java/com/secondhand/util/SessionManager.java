package com.secondhand.util;

import com.secondhand.model.Ad;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static String loggedInUsername = null;
    private static String token = null; // نگهداری توکن کاربر

    // لیستی برای نگهداری موقت آگهی‌های مورد علاقه
    private static final List<Ad> favoriteAds = new ArrayList<>();

    // --- متدهای مربوط به ورود و توکن ---
    public static void login(String username) {
        loggedInUsername = username;
    }

    public static void setToken(String jwtToken) {
        token = jwtToken;
    }

    public static String getToken() {
        return token;
    }

    public static boolean isLoggedIn() {
        return token != null || loggedInUsername != null;
    }

    public static void logout() {
        loggedInUsername = null;
        token = null;
        favoriteAds.clear(); // هنگام خروج، لیست علاقه‌مندی‌ها هم پاک می‌شود
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    // --- متدهای مربوط به علاقه‌مندی‌ها ---
    public static void addToFavorites(Ad ad) {
        // چک می‌کنیم که آگهی تکراری اضافه نشود
        boolean exists = favoriteAds.stream().anyMatch(a -> a.getId().equals(ad.getId()));
        if (!exists) {
            favoriteAds.add(ad);
        }
    }

    public static List<Ad> getFavoriteAds() {
        return favoriteAds;
    }
}