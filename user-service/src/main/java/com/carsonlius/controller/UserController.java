package com.carsonlius.controller;

import com.carsonlius.dto.UserAddDTO;
import com.carsonlius.metrics.MetricsInterface;
import com.carsonlius.pojo.User;
import com.carsonlius.service.UserService;
import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    private UserService userService;

    @PostMapping("/add")
    public Integer add(UserAddDTO addDTO) {
        return (int) (System.currentTimeMillis() / 1000);
    }


    @GetMapping("/userInfos")
    @Timed(value = "time.userInfo", longTask = true)
    public List<User> userInfos() {
        MetricsInterface.METRICS_DEMO_COUNT.increment();
        try {
            Thread.sleep(2000L + (long)Math.random() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        return userService.userInfos();
        return new ArrayList<>();
    }
}
