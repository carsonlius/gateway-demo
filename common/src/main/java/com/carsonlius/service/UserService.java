package com.carsonlius.service;

import com.carsonlius.dto.UserAddDTO;
import com.carsonlius.pojo.User;

import java.util.List;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/8/25 15:57
 * @company
 * @description
 */
public interface UserService {
    User userInfo(Integer id);

    /**
     * 添加新用户，返回新添加的用户编号
     *
     * @param addDTO 添加的用户信息
     * @return 用户编号
     */
    Integer add(UserAddDTO addDTO);

    List<User> userInfos();
}
