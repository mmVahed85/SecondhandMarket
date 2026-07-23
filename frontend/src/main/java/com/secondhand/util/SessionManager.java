package com.secondhand.util;

public class SessionManager {
    private static String loggedInUsername = null;
    private static String token = null; // نگهداری توکن کاربر
    private static String role = null;

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
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static void setRole(String newrole) {
        role = newrole;
    }

    public static String getRole() {
        return role;
    }
}