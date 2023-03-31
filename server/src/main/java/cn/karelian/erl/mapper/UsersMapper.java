/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.mapper;

import cn.karelian.erl.entity.Users;

/**
 * <p>
 * 管理用户的表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface UsersMapper extends ErlMapper<Users> {
	void getUserMsgById(long id);
}
