package com.football.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.football.entity.Player;
import com.football.mapper.PlayerMapper;
import com.football.service.PlayerService;
import org.springframework.stereotype.Service;

/**
 * 球员Service实现类
 */
@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements PlayerService {

    @Override
    public Page<Player> queryPage(Integer current, Integer size, String name, Integer teamId) {
        Page<Player> page = new Page<>(current, size);
        QueryWrapper<Player> wrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        if (teamId != null) {
            wrapper.eq("team_id", teamId);
        }
        return this.page(page, wrapper);
    }

    @Override
    public Player selectById(Integer id) {
        return this.getById(id);
    }

    @Override
    public boolean save(Player player) {
        return super.save(player);
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }
}

