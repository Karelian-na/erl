/*
 * @Author: Karelian_na
 */
/*
 * @Author: Karelian_na
 */
package com.karelian.erl.service;

import com.karelian.erl.Exceptions.NullRequestException;
import com.karelian.erl.Exceptions.UnLoginException;
import com.karelian.erl.entity.Permissions;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理权限目录的表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface IPermissionsService extends IService<Permissions> {
	/**
	 * @description: 获取指定url的权限
	 * @param {String} url
	 * @return {Permissions}
	 */
	public Permissions getByUrl(String url);

	
	/**
	 * @description: 获取指定权限id的权限的类型
	 * @param {Integer} id
	 * @return {Byte}
	 */
	public Byte getTypeById(Integer id);


	// /**
	//  * @description: 获取指定权限id的父权限的类型
	//  * @param {Integer} id
	//  * @return {Byte}
	//  */
	// public Byte getPTypeById(Integer id);


	/**
	 * @description: 获取指定权限id的父权限id
	 * @param {Integer} id
	 * @return {Interger}
	 */
	public Integer getPidById(Integer id);


	/**
	 * @description: 检测权限类型关联是否正确
	 * @param {Byte} pType 父权限的类型
	 * @param {Byte} curType 子权限的类型
	 * @return {Boolean}
	 */
	public boolean checkTypeAssoc(Byte pType, Byte curType);


	/**
	 * @description: 检测当前用户是否已被授权给定id的权限
	 * @param {Integer} id 检测权限的id
	 * @return {Boolean}
	 * @throws NullRequestException
	 * @throws UnLoginException
	 */
	public boolean isAuthorized(Integer id) throws NullRequestException, UnLoginException;


	/**
	 * @description: 检测给定权限id与父id是否有循环关系
	 * @param {Integer} id 检测权限的id
	 * @param {Integer} pid 检测权限的pid
	 * @return {Boolean}
	 */
	public boolean hasCircularRelationship(Integer id, Integer pid);


	/**
	 * @description: 获取给定权限id的权限的层级
	 * @param {Integer} pid 检测权限的id
	 * @return {Boolean}
	 */
	public Byte getLevel(Integer pid);
}
