package com.football.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.football.common.Result;
import com.football.entity.User;
import com.football.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页查询用户
     */
    @PostMapping("/query_page")
    public Result<Page<User>> queryPage(@RequestParam(defaultValue = "1") Integer current,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) String username) {
        Page<User> page = userService.queryPage(current, size, username);
        return Result.success(page);
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/select_by_id")
    public Result<User> selectById(@RequestParam Integer id) {
        User user = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * 保存用户
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody User user) {
        boolean result = userService.save(user);
        return Result.success(result);
    }

    /**
     * 根据ID删除用户
     */
    @PostMapping("/delete_by_id")
    public Result<Boolean> deleteById(@RequestParam Integer id) {
        boolean result = userService.deleteById(id);
        return Result.success(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            // 设置用户上下文
            com.football.common.UserContext.setUser(user);
            return Result.success("登录成功", user);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody User user) {
        boolean result = userService.save(user);
        return Result.success(result);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    public Result<User> getCurrentUser() {
        User user = com.football.common.UserContext.getUser();
        if (user == null || user.getId() == null) {
            // 如果没有用户，返回游客
            User guest = new User();
            guest.setRole(0);
            return Result.success(guest);
        }
        return Result.success(user);
    }
}

