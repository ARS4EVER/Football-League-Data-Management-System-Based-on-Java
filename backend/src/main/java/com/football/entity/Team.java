package com.football.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 球队实体类
 */
@Data
@TableName("team")
public class Team {
    @TableId(type = IdType.AUTO)
    private Integer id;           // 球队ID
    private String name;          // 球队名称
    private String city;          // 所在城市
    private String coach;         // 主教练
    private String homeStadium;   // 主场
    private Integer foundedYear;  // 成立年份
    private Integer status;       // 状态 1-启用 0-禁用
    private LocalDateTime createTime; // 创建时间
}

