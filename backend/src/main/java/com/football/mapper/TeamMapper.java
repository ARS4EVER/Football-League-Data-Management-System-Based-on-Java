package com.football.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.football.entity.Team;
import org.apache.ibatis.annotations.Mapper;

/**
 * 球队Mapper接口
 */
@Mapper
public interface TeamMapper extends BaseMapper<Team> {
}

