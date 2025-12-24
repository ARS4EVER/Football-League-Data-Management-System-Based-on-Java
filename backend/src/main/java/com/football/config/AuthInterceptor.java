package com.football.config;

import com.football.common.UserContext;
import com.football.entity.User;
import com.football.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 允许登录和注册接口
        String uri = request.getRequestURI();
        if (uri.contains("/user/login") || uri.contains("/user/register")) {
            return true;
        }

        // 从请求头或参数中获取用户名（简单实现，实际项目中应使用Token）
        String username = request.getHeader("username");
        if (username == null || username.isEmpty()) {
            username = request.getParameter("username");
        }

        if (username == null || username.isEmpty()) {
            // 如果没有用户名，设置为游客
            User guest = new User();
            guest.setRole(0); // 游客
            UserContext.setUser(guest);
            return true;
        }

        // 查询用户信息
        User user = userService.getByUsername(username);
        if (user == null) {
            // 用户不存在，设置为游客
            User guest = new User();
            guest.setRole(0); // 游客
            UserContext.setUser(guest);
            return true;
        }

        // 设置用户上下文
        UserContext.setUser(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理线程变量
        UserContext.clear();
    }
}

