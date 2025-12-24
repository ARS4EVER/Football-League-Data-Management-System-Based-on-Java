package com.football.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.football.entity.MatchRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 比赛记录Mapper接口
 */
@Mapper
public interface MatchRecordMapper extends BaseMapper<MatchRecord> {
}

