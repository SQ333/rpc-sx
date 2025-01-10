package com.sx.example.common.service;

import com.sx.example.common.model.User;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 获取用户
     *
     * @param user 用户
     * @return 用户名
     */
    User getUser(User user);

    /**
     * 获取数字
     *
     * @return 数字
     */
    default short getNumber() {
        return 1;
    }
}
