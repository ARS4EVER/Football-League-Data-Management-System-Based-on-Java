package com.football.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.football.entity.Player;

/**
 * 球员Service接口
 */
public interface PlayerService extends IService<Player> {
    
    /**
     * 分页查询球员
     */
    Page<Player> queryPage(Integer current, Integer size, String name, Integer teamId);
    
    /**
     * 根据ID查询球员
     */
    Player selectById(Integer id);
    
    /**
     * 保存球员
     */
    boolean save(Player player);
    
    /**
     * 根据ID删除球员
     */
    boolean deleteById(Integer id);
}

