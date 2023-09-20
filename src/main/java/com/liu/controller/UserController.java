package com.liu.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.common.Constants;
import com.liu.common.Result;
import com.liu.controller.dto.UserDTO;
import com.liu.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.liu.service.IUserService;
import com.liu.entity.User;


import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 传奇
 * @since 2023-04-07
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * {table.serviceName}  == UserService
     * {entity} == User
     * {table.entityPath} == user
     */
    /*
    * 登录接口
    * @RequestBody 可以把前端传递参数转为对象
    * */
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password))
        {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto = userService.login(userDTO);

        System.out.println(dto);
        return Result.success(dto);
    }
    //新增或修改
    @PostMapping
    public Boolean save(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    //注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        return Result.success(userService.register(userDTO));
    }

    //删除
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return userService.removeById(id);
    }

    //查询所有
    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }

    //通过id查询
    @GetMapping("/{id}")
    public User findOne(@PathVariable Integer id) {
        return userService.getById(id);
    }

    //通过username查询
    @GetMapping("/username/{username}")
    public User findOne(@PathVariable String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> one = userQueryWrapper.eq("username", username);
        return userService.getOne(one);
    }

    //分页
    @GetMapping("/page")
    public Page<User> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String email,
                               @RequestParam(defaultValue = "") String address) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            userQueryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            userQueryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            userQueryWrapper.like("address", address);
        }
        //获取当前用户信息
        User currentUser = TokenUtils.getCurrentUser();
        System.out.println(currentUser.getNickname());

        userQueryWrapper.orderByDesc("id");
        return userService.page(new Page<>(pageNum, pageSize), userQueryWrapper);
    }

    //批量删除数据
    @PostMapping("/del/batch")
    public boolean deleteBath(@RequestBody List<Integer> ids) {
        return userService.removeByIds(ids);
    }

    /*
    * 导出接口
    * */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        //从数据库查询出所有的数据
        List<User> list = userService.list();
        System.out.println("list==>"+list);
        // 通过工具类创建writer，在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器的响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String filename = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+filename+".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);

        // 关闭writer，释放内存
        out.close();
        writer.close();

    }

    /*
    * 导入接口
    * */
    @PostMapping("/import")
    public void imp(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //自定义标题别名
        reader.addHeaderAlias("用户名", "username");
        reader.addHeaderAlias("密码", "password");
        reader.addHeaderAlias("昵称", "nickname");
        reader.addHeaderAlias("邮箱", "email");
        reader.addHeaderAlias("电话", "phone");
        reader.addHeaderAlias("地址", "address");
        reader.addHeaderAlias("创建时间", "createTime");
        reader.addHeaderAlias("头像", "avatarUrl");
        List<User> list = reader.readAll(User.class);
        userService.saveBatch(list);
        System.out.println("导入lis==》"+list);

    }


}

