/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.mapper;

import cn.karelian.erl.entity.UserPermAssoc;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

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
	@Delete("DELETE FROM user_perm_assoc WHERE (uid = #{uid} AND pid = #{pid})")
	boolean deleteByUnionKey(Long uid, Integer pid);

	@Insert("INSERT INTO user_perm_assoc VALUES(#{uid}, #{pid}, #{authorize}) ON DUPLICATE KEY UPDATE authorize = #{authorize}")
	boolean insertOrUpdateByUnionKey(Long uid, Integer pid, Byte authorize);

	@Select("SELECT authorize FROM user_perm_assoc WHERE uid = #{uid} AND pid = #{pid}")
	Boolean isAuthorized(Integer pid, long uid);
}
