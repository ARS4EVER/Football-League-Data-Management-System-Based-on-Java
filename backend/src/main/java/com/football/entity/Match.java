package com.football.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 比赛实体类
 */
@Data
@TableName("`match`")
public class Match {
    @TableId(type = IdType.AUTO)
    private Integer id;           // 比赛ID
    private Integer leagueId;     // 联赛ID
    private Integer homeTeamId;   // 主队ID
    private Integer awayTeamId;   // 客队ID
    private LocalDateTime matchDate; // 比赛时间
    private Integer homeScore;    // 主队得分
    private Integer awayScore;    // 客队得分
    private String stadium;       // 比赛场地
    private Integer status;       // 状态 1-启用 0-禁用
    private LocalDateTime createTime; // 创建时间
}

