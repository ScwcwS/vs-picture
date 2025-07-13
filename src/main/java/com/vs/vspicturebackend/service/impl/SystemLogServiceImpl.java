package com.vs.vspicturebackend.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.google.gson.annotations.Until;
import com.vs.vspicturebackend.annotation.AuthCheck;
import com.vs.vspicturebackend.exception.BusinessException;
import com.vs.vspicturebackend.exception.ErrorCode;
import com.vs.vspicturebackend.mapper.SystemLogMapper;
import com.vs.vspicturebackend.model.dto.log.LogAddRequest;
import com.vs.vspicturebackend.model.entity.SystemLog;
import com.vs.vspicturebackend.model.entity.User;
import com.vs.vspicturebackend.model.vo.LogVO;
import com.vs.vspicturebackend.service.SystemLogService;
import com.vs.vspicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author scw158
* @description 针对表【system_log(系统日志表)】的数据库操作Service实现
* @createDate 2025-07-13 22:18:34
*/
@Service
@Slf4j
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog>
    implements SystemLogService {


    @Resource
    private UserService userService;

    @Override
    @AuthCheck(mustRole = "admin")
    public boolean addLog(String content, HttpServletRequest request) {
        // 获取userId
        Long userId = checkUserLogin(request);
        SystemLog systemLog = new SystemLog();
        systemLog.setContent(content);
        systemLog.setUserId(userId);
        boolean save = this.save(systemLog);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加系统日志失败");
        }
        return true;
    }

    @Override
    @Transactional
    public boolean markLogAsViewed(Long logId, HttpServletRequest request) {
        Long userId = checkUserLogin(request);
//        QueryWrapper<SystemLog> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", logId);
        SystemLog systemLog = this.getById(logId);
        if (systemLog == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "当前系统日志不存在");
        }
        String viewedBy = systemLog.getViewedBy();
        log.info("viewedBy: {}", viewedBy);
        HashSet<String> viewedBySet  = null;
        if (StringUtils.isBlank(viewedBy)) {
            viewedBySet = new HashSet<>();
        } else {
            // 将viewedBy转为HashSet
            viewedBySet = new HashSet<>(JSONUtil.toList(viewedBy, String.class));

        }
        log.info("viewedBy: {}", viewedBy);
        log.info("systemLog: {}", systemLog);

        // 如果已经标记过，不再重复标记
        if (!viewedBySet.contains(String.valueOf(userId))) {
            viewedBySet.add(String.valueOf(userId));
            // 将Hashset转为JSON字符串
            viewedBy = JSONUtil.toJsonStr(viewedBySet);
            systemLog.setViewedBy(viewedBy);
            boolean result = this.updateById(systemLog);
            if (!result)  {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "标记系统日志失败");
            }
        }
        return true;
    }

    @Override
    public boolean checkLogViewStatus(Long logId, HttpServletRequest request) {
        Long userId = checkUserLogin(request);
        SystemLog systemLog = this.getById(logId);
        if (systemLog == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "当前系统日志记录不存在");
        }
        String viewedBy = systemLog.getViewedBy();
//        if (StringUtils.isBlank(viewedBy)) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "当前系统日志为空");
//        }
        HashSet<String> viewedBySet = new HashSet<>(JSONUtil.toList(viewedBy, String.class));
        return viewedBySet.contains(String.valueOf(userId));

    }

    @Override
    public List<LogVO> getLatestLogs(int count) {
        // 使用MyBatis-Plus的LambdaQueryWrapper构建查询
        List<SystemLog> SystemLogList = this.lambdaQuery()
                .orderByDesc(SystemLog::getCreateTime) // 按创建时间降序
                .last("LIMIT " + count)                // 限制条数
                .list();
        // 将SystemLogList对象转换为List<LogVO>
        return SystemLogList.stream().map(SystemLog -> {
            LogVO logVO = new LogVO();
            logVO.setId(SystemLog.getId());
            logVO.setContent(SystemLog.getContent());
            logVO.setCreateTime(SystemLog.getCreateTime());
            logVO.setUpdateTime(SystemLog.getUpdateTime());
            return logVO;
        }).collect(Collectors.toList());
    }

    // 检查用户是否登录
    private Long checkUserLogin(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"请先登录");
        }
        return userId;
    }
}




