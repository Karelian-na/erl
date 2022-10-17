/*
 * @Author: Karelian_na
 */
package com.karelian.erl.service;

import com.karelian.erl.entity.UserRoleAssoc;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
public interface IUserRoleAssocService extends IService<UserRoleAssoc> {
	public List<Byte> getRoles(long uid);

	public boolean saveBatch(Long uid, List<Byte> rids);
}
