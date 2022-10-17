package com.karelian.erl.mapper;

import com.karelian.erl.entity.RolePermAssoc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16s
 */
public interface RolePermAssocMapper extends BaseMapper<RolePermAssoc> {
    boolean insertByUnionKey(Byte rid, Integer pid);

    boolean deleteByUnionKey(Byte rid, Integer pid);
}
