package com.football.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("`user`")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;           // 用户ID
    private String username;      // 用户名
    private String password;      // 密码
    private String realName;      // 真实姓名
    private String phone;         // 手机号
    private Integer role;         // 角色 0-游客 1-赛事组织者 2-球队工作人员
    private Integer teamId;       // 所属球队ID（仅球队工作人员需要）
    private Integer status;       // 状态 1-启用 0-禁用
    private LocalDateTime createTime; // 创建时间
}

