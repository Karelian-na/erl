package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.UnLoginException;
import cn.karelian.erl.entity.Permissions;

import java.util.List;
import java.util.Map;

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
	// * @description: 获取指定权限id的父权限的类型
	// * @param {Integer} id
	// * @return {Byte}
	// */
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
	public boolean isAuthorized(Permissions permission) throws NullRequestException, UnLoginException;

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

	/**
	 * @description: 获取当前用户授权的菜单权限
	 * @return {Permissions}
	 */
	public List<Permissions> getAuthorizedMenu(Long uid);

	/**
	 * @description: 获取指定用户id的指定权限id的兄弟page类型的权限
	 * @param {Long}    uid 用户的id
	 * @param {Integer} pid 当前page类型的权限的id
	 * @return {*}
	 */
	public List<Permissions> getAuthorizedBrotherPages(Long uid, Integer pid);

	/**
	 * @description: 获取 指定用户(uid)的 指定page类型的权限(pid)的 oper类型的权限
	 * @param {Long}        uid 用户的id
	 * @param {Permissions} permission 当前page类型的权限
	 * @return {*}
	 */
	public List<Permissions> getAuthorizedOperationPermissions(Long uid, Permissions permission);

	public List<Map<String, Object>> getAuthorizedPermissions(Long uid);

	public Result update(Permissions permission);

	public Result add(Permissions permission) throws NullRequestException;

	public List<Map<String, Object>> getAuthorizablePermissions();
}
