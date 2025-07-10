package com.vs.vspicturebackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通过删除请求
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
