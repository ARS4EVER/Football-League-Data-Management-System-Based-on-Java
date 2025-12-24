package com.football.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 球员实体类
 */
@Data
@TableName("player")
public class Player {
    @TableId(type = IdType.AUTO)
    private Integer id;           // 球员ID
    private Integer teamId;       // 球队ID
    private String name;          // 球员姓名
    private Integer number;       // 球衣号码
    private String position;      // 位置
    private Integer age;          // 年龄
    private String nationality;   // 国籍
    private BigDecimal height;    // 身高(米)
    private BigDecimal weight;    // 体重(公斤)
    private String dominantFoot;  // 惯用脚 左/右
    private Integer goals;        // 进球数
    private Integer assists;      // 助攻数
    private Integer appearances;  // 出场数
    private Integer status;       // 状态 1-启用 0-禁用
    private LocalDateTime createTime; // 创建时间
}

