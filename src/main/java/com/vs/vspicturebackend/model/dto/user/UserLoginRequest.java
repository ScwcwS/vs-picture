package com.vs.vspicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3309719917861743930L;

    private String userAccount;

    private String userPassword;

}
