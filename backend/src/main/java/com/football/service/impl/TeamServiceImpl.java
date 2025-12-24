package com.football.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.football.entity.Team;
import com.football.mapper.TeamMapper;
import com.football.service.TeamService;
import org.springframework.stereotype.Service;

/**
 * 球队Service实现类
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Override
    public Page<Team> queryPage(Integer current, Integer size, String name) {
        Page<Team> page = new Page<>(current, size);
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        return this.page(page, wrapper);
    }

    @Override
    public Team selectById(Integer id) {
        return this.getById(id);
    }

    @Override
    public boolean save(Team team) {
        return super.save(team);
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }
}

