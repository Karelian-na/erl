/*
 * @Author: Karelian_na
 */
package com.karelian.erl.mapper;

import com.karelian.erl.entity.Permissions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 管理权限目录的表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface PermissionsMapper extends BaseMapper<Permissions> {
	public void getTypeById(long id);
}
