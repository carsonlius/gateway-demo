package com.carsonlius.controller;

import com.carsonlius.pojo.User;
import com.carsonlius.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/9/10 21:09
 * @company
 * @description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;


    @GetMapping("/byId")
    public User byId(@RequestParam("id") Integer id){
        User user =  userService.userInfo(id);


        return user;
    }
}
