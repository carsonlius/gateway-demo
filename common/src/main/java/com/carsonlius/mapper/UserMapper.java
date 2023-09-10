package com.carsonlius.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carsonlius.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/8/29 17:18
 * @company
 * @description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
