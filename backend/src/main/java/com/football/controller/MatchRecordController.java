package com.football.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.football.common.PermissionChecker;
import com.football.common.Result;
import com.football.entity.MatchRecord;
import com.football.service.MatchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 比赛记录控制器
 */
@RestController
@RequestMapping("/matchRecord")
public class MatchRecordController {

    @Autowired
    private MatchRecordService matchRecordService;

    /**
     * 分页查询比赛记录
     */
    @PostMapping("/query_page")
    public Result<Page<MatchRecord>> queryPage(@RequestParam(defaultValue = "1") Integer current,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              @RequestParam(required = false) Integer matchId) {
        Page<MatchRecord> page = matchRecordService.queryPage(current, size, matchId);
        return Result.success(page);
    }

    /**
     * 根据ID查询比赛记录
     */
    @GetMapping("/select_by_id")
    public Result<MatchRecord> selectById(@RequestParam Integer id) {
        MatchRecord matchRecord = matchRecordService.selectById(id);
        return Result.success(matchRecord);
    }

    /**
     * 保存比赛记录
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody MatchRecord matchRecord) {
        if (!PermissionChecker.canWrite("match")) {
            return Result.error("无权限操作");
        }
        boolean result = matchRecordService.save(matchRecord);
        return Result.success(result);
    }

    /**
     * 根据ID删除比赛记录
     */
    @PostMapping("/delete_by_id")
    public Result<Boolean> deleteById(@RequestParam Integer id) {
        if (!PermissionChecker.canWrite("match")) {
            return Result.error("无权限操作");
        }
        boolean result = matchRecordService.deleteById(id);
        return Result.success(result);
    }
}

