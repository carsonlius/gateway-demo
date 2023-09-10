package com.carsonlius.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/9/9 22:44
 * @company
 * @description
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/testGetDemo")
   public String testGetDemo(){
       return "testGetDemo method";
   }
}
