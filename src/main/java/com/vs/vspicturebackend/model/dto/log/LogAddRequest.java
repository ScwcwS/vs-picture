package com.vs.vspicturebackend.model.dto.log;


import lombok.Data;

import java.io.Serializable;

@Data
public class LogAddRequest implements Serializable {
    /**
     * 日志内容
     */
    private String content;


    private static final long serialVersionUID = 1L;

}
