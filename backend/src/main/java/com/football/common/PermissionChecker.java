package com.football.common;

import com.football.entity.Player;

/**
 * 权限检查工具类
 */
public class PermissionChecker {

    /**
     * 检查用户是否有读取权限（所有登录用户都有读取权限）
     */
    public static boolean canRead() {
        Integer role = UserContext.getUserRole();
        return role != null;
    }

    /**
     * 检查用户是否有创建/更新/删除权限
     * @param resourceType 资源类型：team, player, match
     */
    public static boolean canWrite(String resourceType) {
        Integer role = UserContext.getUserRole();
        if (role == null) {
            return false;
        }

        // 游客只能读取
        if (role == UserRole.GUEST) {
            return false;
        }

        // 赛事组织者可以对所有资源进行CRUD
        if (role == UserRole.EVENT_ORGANIZER) {
            return true;
        }

        // 球队工作人员只能对球员进行CRUD
        if (role == UserRole.TEAM_STAFF) {
            return "player".equals(resourceType);
        }

        return false;
    }

    /**
     * 检查用户是否可以操作该球员（球队工作人员只能操作自己球队的球员）
     */
    public static boolean canOperatePlayer(Player player) {
        Integer role = UserContext.getUserRole();
        if (role == null) {
            return false;
        }

        // 游客不能操作
        if (role == UserRole.GUEST) {
            return false;
        }

        // 赛事组织者可以操作所有球员
        if (role == UserRole.EVENT_ORGANIZER) {
            return true;
        }

        // 球队工作人员只能操作自己球队的球员
        if (role == UserRole.TEAM_STAFF) {
            Integer userTeamId = UserContext.getUserTeamId();
            return userTeamId != null && userTeamId.equals(player.getTeamId());
        }

        return false;
    }

    /**
     * 检查用户是否可以操作该球员（通过teamId检查）
     */
    public static boolean canOperatePlayerByTeamId(Integer playerTeamId) {
        Integer role = UserContext.getUserRole();
        if (role == null) {
            return false;
        }

        // 游客不能操作
        if (role == UserRole.GUEST) {
            return false;
        }

        // 赛事组织者可以操作所有球员
        if (role == UserRole.EVENT_ORGANIZER) {
            return true;
        }

        // 球队工作人员只能操作自己球队的球员
        if (role == UserRole.TEAM_STAFF) {
            Integer userTeamId = UserContext.getUserTeamId();
            return userTeamId != null && userTeamId.equals(playerTeamId);
        }

        return false;
    }
}

