package com.football.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.football.entity.League;
import org.apache.ibatis.annotations.Mapper;

/**
 * 联赛Mapper接口
 */
@Mapper
public interface LeagueMapper extends BaseMapper<League> {
}

