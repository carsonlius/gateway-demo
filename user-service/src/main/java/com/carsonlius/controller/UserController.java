package com.carsonlius.controller;

import com.carsonlius.dto.UserAddDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/9/11 11:25
 * @company
 * @description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/add")
    public Integer add(UserAddDTO addDTO) {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
