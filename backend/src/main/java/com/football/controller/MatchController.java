package com.football.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.football.common.PermissionChecker;
import com.football.common.Result;
import com.football.entity.Match;
import com.football.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 比赛控制器
 */
@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    /**
     * 分页查询比赛
     */
    @PostMapping("/query_page")
    public Result<Page<Match>> queryPage(@RequestParam(defaultValue = "1") Integer current,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) Integer leagueId) {
        Page<Match> page = matchService.queryPage(current, size, leagueId);
        return Result.success(page);
    }

    /**
     * 根据ID查询比赛
     */
    @GetMapping("/select_by_id")
    public Result<Match> selectById(@RequestParam Integer id) {
        Match match = matchService.selectById(id);
        return Result.success(match);
    }

    /**
     * 保存比赛
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Match match) {
        if (!PermissionChecker.canWrite("match")) {
            return Result.error("无权限操作");
        }
        boolean result = matchService.save(match);
        return Result.success(result);
    }

    /**
     * 根据ID删除比赛
     */
    @PostMapping("/delete_by_id")
    public Result<Boolean> deleteById(@RequestParam Integer id) {
        if (!PermissionChecker.canWrite("match")) {
            return Result.error("无权限操作");
        }
        boolean result = matchService.deleteById(id);
        return Result.success(result);
    }
}

