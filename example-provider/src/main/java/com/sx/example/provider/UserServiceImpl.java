package com.sx.example.provider;

import com.sx.example.common.model.User;
import com.sx.example.common.service.UserService;

/**
 * 用户服务实现
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("name: " + user.getName());
        return user;
    }
}
