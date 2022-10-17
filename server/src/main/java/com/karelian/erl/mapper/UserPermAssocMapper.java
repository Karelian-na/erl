/*
 * @Author: Karelian_na
 */
package com.karelian.erl.mapper;

import com.karelian.erl.entity.UserPermAssoc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户权限关联表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
public interface UserPermAssocMapper extends BaseMapper<UserPermAssoc> {
	// void removeBatchByUnionKey(List<Map<String, Object>> data);
	boolean deleteByUnionKey(Long uid, Integer pid);

	boolean insertOrUpdateByUnionKey(Long uid, Integer pid, Byte authorize);
}
