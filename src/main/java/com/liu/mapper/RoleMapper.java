package com.liu.mapper;

import com.liu.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 传奇
 * @since 2023-06-09
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where flag =#{flag}")
    Integer selectRoleIdByFlag(@PathVariable("flag") String flag);
}
