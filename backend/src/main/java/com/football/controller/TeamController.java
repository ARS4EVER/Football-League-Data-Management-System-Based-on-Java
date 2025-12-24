package com.football.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.football.common.PermissionChecker;
import com.football.common.Result;
import com.football.entity.Team;
import com.football.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 球队控制器
 */
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * 分页查询球队
     */
    @PostMapping("/query_page")
    public Result<Page<Team>> queryPage(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String name) {
        Page<Team> page = teamService.queryPage(current, size, name);
        return Result.success(page);
    }

    /**
     * 根据ID查询球队
     */
    @GetMapping("/select_by_id")
    public Result<Team> selectById(@RequestParam Integer id) {
        Team team = teamService.selectById(id);
        return Result.success(team);
    }

    /**
     * 保存球队
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Team team) {
        if (!PermissionChecker.canWrite("team")) {
            return Result.error("无权限操作");
        }
        boolean result = teamService.save(team);
        return Result.success(result);
    }

    /**
     * 根据ID删除球队
     */
    @PostMapping("/delete_by_id")
    public Result<Boolean> deleteById(@RequestParam Integer id) {
        if (!PermissionChecker.canWrite("team")) {
            return Result.error("无权限操作");
        }
        boolean result = teamService.deleteById(id);
        return Result.success(result);
    }
}

