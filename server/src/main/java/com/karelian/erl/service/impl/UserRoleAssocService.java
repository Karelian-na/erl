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
		lqw.select(UserRoleAssoc::getRid).eq(UserRoleAssoc::getUid, uid);
		return this.baseMapper.selectObjs(lqw).stream().map(obj -> (byte)((Integer)obj).intValue()).collect(Collectors.toList());
	}

	@Override
	public boolean saveBatch(Long uid, List<Byte> rids) {
		List<UserRoleAssoc> datas = new ArrayList<>();
		for (Byte rid : rids) {
			UserRoleAssoc ura = new UserRoleAssoc();
			ura.setUid(uid);
			ura.setRid(rid);
			datas.add(ura);
		}
		return this.baseMapper.saveBatch(datas);
	}
}
