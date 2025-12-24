package com.football.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.football.entity.League;
import com.football.mapper.LeagueMapper;
import com.football.service.LeagueService;
import org.springframework.stereotype.Service;

/**
 * 联赛Service实现类
 */
@Service
public class LeagueServiceImpl extends ServiceImpl<LeagueMapper, League> implements LeagueService {

    @Override
    public Page<League> queryPage(Integer current, Integer size, String name) {
        Page<League> page = new Page<>(current, size);
        QueryWrapper<League> wrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        return this.page(page, wrapper);
    }

    @Override
    public League selectById(Integer id) {
        return this.getById(id);
    }

    @Override
    public boolean save(League league) {
        return super.save(league);
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }
}

