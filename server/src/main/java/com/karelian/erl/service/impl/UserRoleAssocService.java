/*
 * @Author: Karelian_na
 */
package com.karelian.erl.service.impl;

import com.karelian.erl.entity.UserRoleAssoc;
import com.karelian.erl.mapper.UserRoleAssocMapper;
import com.karelian.erl.service.IUserRoleAssocService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
@Service
public class UserRoleAssocService extends ServiceImpl<UserRoleAssocMapper, UserRoleAssoc>
		implements IUserRoleAssocService {

	@Override
	public List<Byte> getRoles(long uid) {
		LambdaQueryWrapper<UserRoleAssoc> lqw = new LambdaQueryWrapper<UserRoleAssoc>();
		lqw.select(UserRoleAssoc.class, i -> i.getProperty().equals("rid"))
				.eq(UserRoleAssoc::getUid, uid);
		return this.baseMapper.selectObjs(lqw).stream().map(obj -> (byte)((Integer)obj).intValue()).collect(Collectors.toList());
	}

}
