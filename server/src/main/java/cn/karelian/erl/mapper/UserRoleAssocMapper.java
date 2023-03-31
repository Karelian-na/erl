/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.mapper;

import cn.karelian.erl.entity.UserRoleAssoc;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
public interface UserRoleAssocMapper extends BaseMapper<UserRoleAssoc> {
	@Delete("DELETE FROM user_role_assoc WHERE uid = #{uid} AND rid = #{rid}")
	public boolean deleteByUnionKey(Long uid, Byte rid);

	@Insert("INSERT INTO user_role_assoc VALUES(#{uid}, #{rid})")
	public boolean insertByUnionKey(Long uid, Byte rid);

	public boolean insertBatchByUnionKey(Map<String, List<?>> params);

	@Select("SELECT rid FROM user_role_assoc WHERE uid = #{uid}")
	public List<Byte> getRoles(Long uid);
}
