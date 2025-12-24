package com.football.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.football.entity.League;

/**
 * 联赛Service接口
 */
public interface LeagueService extends IService<League> {
    
    /**
     * 分页查询联赛
     */
    Page<League> queryPage(Integer current, Integer size, String name);
    
    /**
     * 根据ID查询联赛
     */
    League selectById(Integer id);
    
    /**
     * 保存联赛
     */
    boolean save(League league);
    
    /**
     * 根据ID删除联赛
     */
    boolean deleteById(Integer id);
}

