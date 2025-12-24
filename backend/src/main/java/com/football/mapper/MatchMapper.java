package com.football.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.football.entity.Match;
import org.apache.ibatis.annotations.Mapper;

/**
 * 比赛Mapper接口
 */
@Mapper
public interface MatchMapper extends BaseMapper<Match> {
}

