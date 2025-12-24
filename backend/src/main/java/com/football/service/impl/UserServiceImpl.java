package com.football.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.football.entity.User;
import com.football.mapper.UserMapper;
import com.football.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Page<User> queryPage(Integer current, Integer size, String username) {
        Page<User> page = new Page<>(current, size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like("username", username);
        }
        return this.page(page, wrapper);
    }

    @Override
    public User selectById(Integer id) {
        return this.getById(id);
    }

    @Override
    public boolean save(User user) {
        return super.save(user);
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);
        wrapper.eq("status", 1);
        return this.getOne(wrapper);
    }

    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("status", 1);
        return this.getOne(wrapper);
    }
}

