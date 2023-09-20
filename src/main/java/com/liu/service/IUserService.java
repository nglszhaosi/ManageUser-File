package com.liu.service;

import com.liu.controller.dto.UserDTO;
import com.liu.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 传奇
 * @since 2023-04-07
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);
    User register(UserDTO userDTO);
}
