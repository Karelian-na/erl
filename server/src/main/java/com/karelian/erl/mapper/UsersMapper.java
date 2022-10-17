/*
 * @Author: Karelian_na
 */
package com.karelian.erl.mapper;

import com.karelian.erl.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 管理用户的表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface UsersMapper extends BaseMapper<Users> {
	void getUserMsgById(long id);
}
