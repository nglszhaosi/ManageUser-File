package com.liu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.common.Constants;
import com.liu.common.Result;
import com.liu.entity.Dict;
import com.liu.mapper.DictMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.liu.service.IMenuService;
import com.liu.entity.Menu;


import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 传奇
 * @since 2023-06-11
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private DictMapper dictMapper;

    /**
     * {table.serviceName}  == UserService
     * {entity} == User
     * {table.entityPath} == user
     */
    //新增或修改
    @PostMapping
    public Boolean save(@RequestBody Menu menu) {
        return menuService.saveOrUpdate(menu);
    }

    //删除
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return menuService.removeById(id);
    }

    //查询所有
    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name) {
        return Result.success(menuService.findMenus(name));
    }

    //通过id查询
    @GetMapping("/{id}")
    public Menu findOne(@PathVariable Integer id) {
        return menuService.getById(id);
    }

    //分页
    @GetMapping("/page")
    public Page<Menu> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        return menuService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    //批量删除数据
    @PostMapping("/del/batch")
    public boolean deleteBath(@RequestBody List<Integer> ids) {
        return menuService.removeByIds(ids);
    }

    @GetMapping("/icons")
    public Result getIcons() {
        System.out.println("进入icons");
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(queryWrapper));
    }
}

