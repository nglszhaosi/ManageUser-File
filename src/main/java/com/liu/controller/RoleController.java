package com.liu.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.common.Result;
import com.liu.entity.User;
import com.liu.utils.TokenUtils;
import jdk.nashorn.internal.parser.Token;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.liu.service.IRoleService;
import com.liu.entity.Role;


import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 传奇
 * @since 2023-06-09
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * {table.serviceName}  == UserService
     * {entity} == User
     * {table.entityPath} == user
     */
    //新增或修改
    @PostMapping
    public Boolean save(@RequestBody Role role) {
        return roleService.saveOrUpdate(role);
    }

    //删除
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return roleService.removeById(id);
    }

    //查询所有
    @GetMapping
    public List<Role> findAll() {
        System.out.println("进入查询所有role");
        return roleService.list();
    }

    //通过id查询
    @GetMapping("/{id}")
    public Role findOne(@PathVariable Integer id) {
        return roleService.getById(id);
    }

    //分页
    @GetMapping("/page")
    public Page<Role> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String description) {
        System.out.println("进入page方法");
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            roleQueryWrapper.like("name", name);
        }
        if (!"".equals(description)) {
            roleQueryWrapper.like("description", description);
        }

        roleQueryWrapper.orderByDesc("id");

        return roleService.page(new Page<>(pageNum, pageSize), roleQueryWrapper);
    }

    //批量删除数据
    @PostMapping("/del/batch")
    public boolean deleteBath(@RequestBody List<Integer> ids) {
        return roleService.removeByIds(ids);
    }

    /*
     * 导出接口
     * */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        //从数据库查询出所有的数据
        List<Role> list = roleService.list();
        System.out.println("list==>" + list);
        // 通过工具类创建writer，在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("description", "描述");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //设置浏览器的响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String filename = URLEncoder.encode("角色信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);

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
        reader.addHeaderAlias("名称", "name");
        reader.addHeaderAlias("描述", "description");

        List<Role> list = reader.readAll(Role.class);
        roleService.saveBatch(list);
        System.out.println("导入lis==》" + list);

    }

    /**
     *
     * @param roleId 角色id
     * @param menuIds 菜单id数组
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId,menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {

        return Result.success( roleService.getRoleMenu(roleId));
    }

}

