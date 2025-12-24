package com.football.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.football.entity.MatchRecord;

/**
 * 比赛记录Service接口
 */
public interface MatchRecordService extends IService<MatchRecord> {
    
    /**
     * 分页查询比赛记录
     */
    Page<MatchRecord> queryPage(Integer current, Integer size, Integer matchId);
    
    /**
     * 根据ID查询比赛记录
     */
    MatchRecord selectById(Integer id);
    
    /**
     * 保存比赛记录
     */
    boolean save(MatchRecord matchRecord);
    
    /**
     * 根据ID删除比赛记录
     */
    boolean deleteById(Integer id);
}

