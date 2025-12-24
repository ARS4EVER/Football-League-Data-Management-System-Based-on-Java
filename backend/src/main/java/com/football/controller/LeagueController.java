package com.football.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.football.common.Result;
import com.football.entity.League;
import com.football.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 联赛控制器
 */
@RestController
@RequestMapping("/league")
public class LeagueController {

    @Autowired
    private LeagueService leagueService;

    /**
     * 分页查询联赛
     */
    @PostMapping("/query_page")
    public Result<Page<League>> queryPage(@RequestParam(defaultValue = "1") Integer current,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) String name) {
        Page<League> page = leagueService.queryPage(current, size, name);
        return Result.success(page);
    }

    /**
     * 根据ID查询联赛
     */
    @GetMapping("/select_by_id")
    public Result<League> selectById(@RequestParam Integer id) {
        League league = leagueService.selectById(id);
        return Result.success(league);
    }

    /**
     * 保存联赛
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody League league) {
        boolean result = leagueService.save(league);
        return Result.success(result);
    }

    /**
     * 根据ID删除联赛
     */
    @PostMapping("/delete_by_id")
    public Result<Boolean> deleteById(@RequestParam Integer id) {
        boolean result = leagueService.deleteById(id);
        return Result.success(result);
    }
}

