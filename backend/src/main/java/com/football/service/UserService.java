package com.football.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.football.entity.User;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 分页查询用户
     */
    Page<User> queryPage(Integer current, Integer size, String username);
    
    /**
     * 根据ID查询用户
     */
    User selectById(Integer id);
    
    /**
     * 保存用户
     */
    boolean save(User user);
    
    /**
     * 根据ID删除用户
     */
    boolean deleteById(Integer id);
    
    /**
     * 用户登录
     */
    User login(String username, String password);
    
    /**
     * 根据用户名查询用户（用于拦截器）
     */
    User getByUsername(String username);
}

