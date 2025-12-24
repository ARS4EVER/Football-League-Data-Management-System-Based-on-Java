package com.football.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.football.entity.MatchRecord;
import com.football.mapper.MatchRecordMapper;
import com.football.service.MatchRecordService;
import org.springframework.stereotype.Service;

/**
 * 比赛记录Service实现类
 */
@Service
public class MatchRecordServiceImpl extends ServiceImpl<MatchRecordMapper, MatchRecord> implements MatchRecordService {

    @Override
    public Page<MatchRecord> queryPage(Integer current, Integer size, Integer matchId) {
        Page<MatchRecord> page = new Page<>(current, size);
        QueryWrapper<MatchRecord> wrapper = new QueryWrapper<>();
        if (matchId != null) {
            wrapper.eq("match_id", matchId);
        }
        return this.page(page, wrapper);
    }

    @Override
    public MatchRecord selectById(Integer id) {
        return this.getById(id);
    }

    @Override
    public boolean save(MatchRecord matchRecord) {
        return super.save(matchRecord);
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }
}

