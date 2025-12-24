package com.football.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 联赛实体类
 */
@Data
@TableName("league")
public class League {
    @TableId(type = IdType.AUTO)
    private Integer id;           // 联赛ID
    private String name;          // 联赛名称
    private String season;        // 赛季
    private LocalDate startDate;  // 开始日期
    private LocalDate endDate;    // 结束日期
    private Integer status;       // 状态 1-启用 0-禁用
    private LocalDateTime createTime; // 创建时间
}

