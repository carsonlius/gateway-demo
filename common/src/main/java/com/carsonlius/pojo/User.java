package com.carsonlius.pojo;

import lombok.Data;

import java.io.Serializable;


/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/8/25 16:01
 * @company
 * @description
 */
@Data
public class User implements Serializable {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
