package cn.karelian.erl.mapper;

import cn.karelian.erl.entity.RolePermAssoc;

import java.util.List;

import org.apache.ibatis.annotations.Select;

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

	@Select("SELECT pid from role_perm_assoc WHERE rid = #{rid}")
	List<Integer> getAuthorizedPermissionIds(Integer rid);
}
