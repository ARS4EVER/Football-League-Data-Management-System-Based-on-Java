package com.football.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 比赛记录实体类
 */
@Data
@TableName("match_record")
public class MatchRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;           // 记录ID
    private Integer matchId;      // 比赛ID
    private Integer playerId;     // 球员ID
    private Integer eventType;    // 事件类型 1-进球 2-黄牌 3-红牌 4-助攻
    private Integer eventTime;    // 事件发生时间(分钟)
    private String description;   // 描述
    private LocalDateTime createTime; // 创建时间
}

