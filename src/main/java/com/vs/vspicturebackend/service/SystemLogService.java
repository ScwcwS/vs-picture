package com.vs.vspicturebackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.vspicturebackend.model.entity.SystemLog;
import com.vs.vspicturebackend.model.vo.LogVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author scw158
* @description 针对表【system_log(系统日志表)】的数据库操作Service
* @createDate 2025-07-13 22:18:34
*/


public interface SystemLogService extends IService<SystemLog> {


    /**
     * 添加系统日志
     * @param content
     * @param request
     * @return
     */
    boolean addLog(String content, HttpServletRequest request);



    /**
     * 标记日志为已读
     * @param logId 日志ID
     * @param request 请求
     * @return 是否成功
     */
    boolean markLogAsViewed(Long logId, HttpServletRequest request);


    /**
     * 判断用户是否查看过日志
     * @param logId 日志ID
     * @param request 用户ID
     * @return 查看状态对象
     */
    boolean checkLogViewStatus(Long logId, HttpServletRequest request);


    /**
     * 查询最新的日志
     * @param count 需要查询的条数
     * @return 最新的日志列表
     */
    List<LogVO> getLatestLogs(int count);

}
