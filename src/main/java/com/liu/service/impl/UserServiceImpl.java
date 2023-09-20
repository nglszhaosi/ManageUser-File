package com.liu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.common.Constants;
import com.liu.controller.dto.UserDTO;
import com.liu.entity.Menu;
import com.liu.entity.User;
import com.liu.exception.ServiceException;
import com.liu.mapper.RoleMapper;
import com.liu.mapper.RoleMenuMapper;
import com.liu.mapper.UserMapper;
import com.liu.service.IMenuService;
import com.liu.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 传奇
 * @since 2023-04-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one!=null){
            BeanUtil.copyProperties(one,userDTO,true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);

            String role = one.getRole();//ROLE_ADMIN
            List<Menu> roleMenus = getRoleMenus(role);
            //设置用户菜单列表
            userDTO.setMenus(roleMenus);
            return userDTO;
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }

    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one==null){
            one = new User();
            BeanUtil.copyProperties(userDTO,one,true);
            save(one);//把copy之后的用户对象存储到数据库
        }else {
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return one;
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", userDTO.getUsername()).eq("password", userDTO.getPassword());

        User one;
        try {
            one = getOne(userQueryWrapper);//从数据库查询信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag){
        Integer roleId = roleMapper.selectRoleIdByFlag(roleFlag);
        //当前角色的所有菜单的id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        //查出系统所有的菜单
        List<Menu> menus = menuService.findMenus("");

        List<Menu> roleMenus = new ArrayList<>();

        //筛选当前用户的角色菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            for (Menu child : children) {
                System.out.println("child.getName()==>"+child.getName());
            }
            //removeIf 移除children里面不在menuids集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        System.out.println("roleMenus==>"+roleMenus.toString());
        return roleMenus;
    }
}

