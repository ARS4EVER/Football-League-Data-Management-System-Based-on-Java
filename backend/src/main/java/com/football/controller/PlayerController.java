package com.football.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.football.common.PermissionChecker;
import com.football.common.Result;
import com.football.entity.IdDto;
import com.football.entity.Player;
import com.football.mapper.PlayerMapper;
import com.football.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 球员控制器
 */
@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerMapper playerMapper;

    private static final Map<String, String> DIMENSION_MAPPING = new HashMap<>();
    static {
        DIMENSION_MAPPING.put("goals", "进球数");
        DIMENSION_MAPPING.put("assists", "助攻数");
        DIMENSION_MAPPING.put("appearances", "出场数");
        DIMENSION_MAPPING.put("age", "年龄");
        DIMENSION_MAPPING.put("height", "身高(米)");
        DIMENSION_MAPPING.put("weight", "体重(公斤)");
        DIMENSION_MAPPING.put("number", "球衣号码");
    }

    /**
     * 分页查询球员
     */
    @PostMapping("/query_page")
    public Result<Page<Player>> queryPage(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) Integer teamId) {
        Page<Player> page = playerService.queryPage(current, size, name, teamId);
        return Result.success(page);
    }

    /**
     * 根据ID查询球员
     */
    @GetMapping("/select_by_id")
    public Result<Player> selectById(@RequestParam Integer id) {
        Player player = playerService.selectById(id);
        return Result.success(player);
    }

    /**
     * 保存球员
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Player player) {
        if (!PermissionChecker.canWrite("player")) {
            return Result.error("无权限操作");
        }
        // 如果是球队工作人员，检查是否只能操作自己球队的球员
        if (player.getId() != null) {
            // 更新操作，需要检查权限
            Player existingPlayer = playerService.selectById(player.getId());
            if (existingPlayer != null && !PermissionChecker.canOperatePlayer(existingPlayer)) {
                return Result.error("无权限操作该球员");
            }
        } else {
            // 新增操作，球队工作人员只能添加自己球队的球员
            if (!PermissionChecker.canOperatePlayerByTeamId(player.getTeamId())) {
                return Result.error("无权限操作该球队的球员");
            }
        }
        boolean result = playerService.save(player);
        return Result.success(result);
    }

    /**
     * 根据ID删除球员
     */
    @PostMapping("/delete_by_id")
    public Result<Boolean> deleteById(@RequestParam Integer id) {
        if (!PermissionChecker.canWrite("player")) {
            return Result.error("无权限操作");
        }
        Player player = playerService.selectById(id);
        if (player != null && !PermissionChecker.canOperatePlayer(player)) {
            return Result.error("无权限操作该球员");
        }
        boolean result = playerService.deleteById(id);
        return Result.success(result);
    }

    @GetMapping("/all_list")
    public Result allList(){

        List<Player> playerList = playerMapper.selectList(null);

        return Result.success(playerList);

    }

    @PostMapping("/echarts_analysis")
    public Result echartsAnalysis(@RequestBody IdDto dto){

        try {
            // 1. 基础参数校验
            List<Integer> idList = dto.getIdList();
            List<String> dimensionList = dto.getDimensionList();
            if (idList == null || idList.size() < 2) {
                return Result.error("请选择两名及以上球员进行对比");
            }
            if (dimensionList == null || dimensionList.isEmpty()) {
                return Result.error("请指定至少一个分析维度");
            }

            // 2. 维度合法性校验
            List<String> validDimensions = validateDimensions(dimensionList);
            if (validDimensions.isEmpty()) {
                return Result.error("无有效分析维度，支持的维度：" + DIMENSION_MAPPING.keySet());
            }

            // 3. 查询球员数据
            LambdaQueryWrapper<Player> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Player::getId, idList);
            List<Player> playerList = playerMapper.selectList(queryWrapper);

            // 4. 动态组装ECharts雷达图数据（根据前端传的维度）
            Map<String, Object> echartsData = buildDynamicRadarData(playerList, validDimensions);

            return Result.success(echartsData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取球员分析数据失败：" + e.getMessage());
        }
    }

    /**
     * 校验前端传的维度是否合法（只保留存在的维度）
     */
    private List<String> validateDimensions(List<String> dimensionList) {
        List<String> validDimensions = new ArrayList<>();
        for (String dimension : dimensionList) {
            if (StringUtils.hasText(dimension) && DIMENSION_MAPPING.containsKey(dimension)) {
                validDimensions.add(dimension);
            }
        }
        return validDimensions;
    }


    /**
     * 动态构建雷达图数据
     */
    private Map<String, Object> buildDynamicRadarData(List<Player> playerList, List<String> validDimensions) {
        Map<String, Object> result = new HashMap<>();

        List<String> indicator = new ArrayList<>();
        for (String dimension : validDimensions) {
            indicator.add(DIMENSION_MAPPING.get(dimension));
        }
        result.put("indicator", indicator);

        List<Map<String, Object>> seriesData = new ArrayList<>();
        for (Player player : playerList) {
            Map<String, Object> playerData = new HashMap<>();
            playerData.put("name", player.getName()); // 球员姓名

            List<Number> valueList = new ArrayList<>();
            for (String dimension : validDimensions) {
                Number value = getPlayerDimensionValue(player, dimension);
                valueList.add(value == null ? 0 : value);
            }
            playerData.put("value", valueList);
            seriesData.add(playerData);
        }
        result.put("seriesData", seriesData);

        return result;
    }

    /**
     * 通过反射获取Player对象指定维度的属性值
     */
    private Number getPlayerDimensionValue(Player player, String dimension) {
        try {
            Field field = Player.class.getDeclaredField(dimension);
            field.setAccessible(true); // 允许访问私有字段
            Object value = field.get(player);

            if (value instanceof Integer) {
                return (Integer) value;
            } else if (value instanceof BigDecimal) {
                return ((BigDecimal) value).doubleValue();
            } else {
                return 0;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }


}

