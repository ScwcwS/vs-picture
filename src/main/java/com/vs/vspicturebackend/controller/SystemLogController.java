package com.vs.vspicturebackend.controller;


import com.vs.vspicturebackend.common.BaseResponse;
import com.vs.vspicturebackend.common.ResultUtils;
import com.vs.vspicturebackend.model.dto.log.LogAddRequest;
import com.vs.vspicturebackend.model.entity.SystemLog;
import com.vs.vspicturebackend.model.vo.LogVO;
import com.vs.vspicturebackend.service.SystemLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("/systemLog")
public class SystemLogController {

    @Resource
    private SystemLogService systemLogService;


    @PostMapping("/add")
    public BaseResponse<Boolean> addLog(@RequestBody LogAddRequest logAddRequest, HttpServletRequest request) {
        String content = logAddRequest.getContent();
        boolean result = systemLogService.addLog(content,request);
        return ResultUtils.success(result);
    }

    // 标记用户读取日志
    @PostMapping("/markAsViewed")
    public BaseResponse<Boolean> markLogAsViewed(@RequestParam Long logId,HttpServletRequest request) {
        boolean result = systemLogService.markLogAsViewed(logId,request);
        return ResultUtils.success(result);
    }

    @GetMapping("/checkLogViewStatus")
    public BaseResponse<Boolean> checkLogViewStatus(@RequestParam Long logId,HttpServletRequest request) {
        boolean result = systemLogService.checkLogViewStatus(logId,request);
        return ResultUtils.success(result);
    }
    @GetMapping("/getLatestLogs")
    public BaseResponse<List<LogVO>> getLatestLogs(@RequestParam int count) {
        List<LogVO> result = systemLogService.getLatestLogs(count);
        return ResultUtils.success(result);
    }
}
