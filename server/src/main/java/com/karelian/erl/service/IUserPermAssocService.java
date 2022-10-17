/*
 * @Author: Karelian_na
 */
package com.karelian.erl.service;

import com.karelian.erl.Exceptions.NullRequestException;
import com.karelian.erl.entity.UserPermAssoc;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户权限关联表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
public interface IUserPermAssocService extends IService<UserPermAssoc> {
	/**
	 * @description: 判断当前用户是否已被授权给定权限pid
	 * @param {Integer} pid 验证的权限的id
	 * @return {*}
	 * @throws NullRequestException
	 */
	public boolean isAuthorized(Integer pid) throws NullRequestException;

	/**
	 * @description: 判断给定用户uid是否已被授权给定权限pid
	 * @param {Integer} pid 验证的权限的id
	 * @param {long}    uid 验证的用户的id
	 * @return {*}
	 */
	public Boolean isAuthorized(Integer pid, long uid);


	/**
	 * @description: 给指定用户批量授权指定权限id
	 * @param {Long}          uid
	 * @param {List<Integer>} pids
	 * @return {*}
	 */
	public boolean saveBatch(Long uid, List<Integer> pids);


	/**
	 * @description: 给指定用户批量取消授权指定权限id
	 * @param {Long}          uid
	 * @param {List<Integer>} pids
	 * @return {*}
	 */
	public boolean removeBatch(Long uid, List<Integer> pids);
}
