package com.carsonlius.service.impl;

import com.carsonlius.dto.UserAddDTO;
import com.carsonlius.mapper.UserMapper;
import com.carsonlius.pojo.User;
import com.carsonlius.service.UserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/9/10 10:07
 * @company
 * @description
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User userInfo(Integer id) {
       return userMapper.selectById(id);
    }

    @Override
    public Integer add(UserAddDTO addDTO) {
        return (int) (System.currentTimeMillis() / 1000);
    }

    @Override
    public List<User> userInfos() {
        return userMapper.selectList(null);
    }
}
