package com.football.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.football.entity.Match;
import com.football.mapper.MatchMapper;
import com.football.service.MatchService;
import org.springframework.stereotype.Service;

/**
 * 比赛Service实现类
 */
@Service
public class MatchServiceImpl extends ServiceImpl<MatchMapper, Match> implements MatchService {

    @Override
    public Page<Match> queryPage(Integer current, Integer size, Integer leagueId) {
        Page<Match> page = new Page<>(current, size);
        QueryWrapper<Match> wrapper = new QueryWrapper<>();
        if (leagueId != null) {
            wrapper.eq("league_id", leagueId);
        }
        return this.page(page, wrapper);
    }

    @Override
    public Match selectById(Integer id) {
        return this.getById(id);
    }

    @Override
    public boolean save(Match match) {
        return super.save(match);
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }
}

