package com.football.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.football.entity.Team;

/**
 * 球队Service接口
 */
public interface TeamService extends IService<Team> {
    
    /**
     * 分页查询球队
     */
    Page<Team> queryPage(Integer current, Integer size, String name);
    
    /**
     * 根据ID查询球队
     */
    Team selectById(Integer id);
    
    /**
     * 保存球队
     */
    boolean save(Team team);
    
    /**
     * 根据ID删除球队
     */
    boolean deleteById(Integer id);
}

