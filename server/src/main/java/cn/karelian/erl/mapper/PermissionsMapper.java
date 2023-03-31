/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.mapper;

import cn.karelian.erl.entity.Permissions;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 管理权限目录的表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface PermissionsMapper extends ErlMapper<Permissions> {
	/**
	 * @description: 获取指定url的权限
	 * @param {String} url
	 * @return {Permissions}
	 */
	@Select("SELECT * FROM permissions WHERE url = '${url}'")
	public Permissions getByUrl(String url);

	@Select("SELECT id, name, pid, type FROM permissions ORDER BY pid ASC, type ASC")
	public List<Map<String, Object>> getAuthorizablePermissions();

	public Byte getTypeById(long id);

	public List<Map<String, Object>> getAuthorizedPermissions(Long uid);

	public List<Permissions> getAuthorizedMenu(Long uid);

	public List<Permissions> getAuthorizedBrotherPages(Long uid, Integer pid);

	public List<Permissions> getAuthorizedOperationPermissions(Long uid, Permissions permission);
}
