package com.football.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.football.entity.Player;
import org.apache.ibatis.annotations.Mapper;

/**
 * 球员Mapper接口
 */
@Mapper
public interface PlayerMapper extends BaseMapper<Player> {
}

