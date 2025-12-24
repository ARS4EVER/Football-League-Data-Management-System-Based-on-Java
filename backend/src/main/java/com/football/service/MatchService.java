package com.football.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.football.entity.Match;

/**
 * 比赛Service接口
 */
public interface MatchService extends IService<Match> {
    
    /**
     * 分页查询比赛
     */
    Page<Match> queryPage(Integer current, Integer size, Integer leagueId);
    
    /**
     * 根据ID查询比赛
     */
    Match selectById(Integer id);
    
    /**
     * 保存比赛
     */
    boolean save(Match match);
    
    /**
     * 根据ID删除比赛
     */
    boolean deleteById(Integer id);
}

