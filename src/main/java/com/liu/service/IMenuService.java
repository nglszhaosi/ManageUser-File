package com.liu.service;

import com.liu.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 传奇
 * @since 2023-06-11
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
