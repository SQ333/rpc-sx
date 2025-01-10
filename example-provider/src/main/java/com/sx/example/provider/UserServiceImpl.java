package com.sx.example.provider;

import com.sx.example.common.model.User;
import com.sx.example.common.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户服务实现
 */
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        log.info("Get user: {}", user.getName());
        return user;
    }
}
