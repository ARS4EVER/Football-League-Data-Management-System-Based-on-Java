package com.football.common;

import com.football.entity.User;

/**
 * 用户上下文，用于存储当前登录用户信息
 */
public class UserContext {
    private static final ThreadLocal<User> USER_HOLDER = new ThreadLocal<>();

    public static void setUser(User user) {
        USER_HOLDER.set(user);
    }

    public static User getUser() {
        return USER_HOLDER.get();
    }

    public static void clear() {
        USER_HOLDER.remove();
    }

    public static Integer getUserId() {
        User user = getUser();
        return user != null ? user.getId() : null;
    }

    public static Integer getUserRole() {
        User user = getUser();
        return user != null ? user.getRole() : null;
    }

    public static Integer getUserTeamId() {
        User user = getUser();
        return user != null ? user.getTeamId() : null;
    }
}

