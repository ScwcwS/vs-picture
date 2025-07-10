package com.vs.vspicturebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -1039027553109119003L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;


}
