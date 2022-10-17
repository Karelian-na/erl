package com.karelian.erl.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;
import org.springframework.web.context.request.*;
import com.karelian.erl.Exceptions.NullRequestException;
import com.karelian.erl.entity.UserPermAssoc;
import com.karelian.erl.mapper.UserPermAssocMapper;
import com.karelian.erl.service.IUserPermAssocService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户权限关联表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
@Service
public class UserPermAssocService extends ServiceImpl<UserPermAssocMapper, UserPermAssoc>
		implements IUserPermAssocService {

	@Override
	public boolean isAuthorized(Integer pid) throws NullRequestException {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession();

		Integer uid = (Integer) session.getAttribute("id");

		return isAuthorized(pid, uid);
	}

	@Override
	public Boolean isAuthorized(Integer pid, long uid) {
		LambdaQueryWrapper<UserPermAssoc> lqw = new LambdaQueryWrapper<UserPermAssoc>();
		lqw.select(UserPermAssoc.class,
				i -> i.getProperty().equals("authorize"))
				.eq(UserPermAssoc::getUid, uid)
				.eq(UserPermAssoc::getPid, pid);
		List<Object> objs = this.baseMapper.selectObjs(lqw);
		if (objs.size() != 0) {
			return (Integer) objs.get(0) == 1;
		}
		return null;
	}

	@Override
	public boolean saveBatch(Long uid, List<Integer> pids) {
		List<UserPermAssoc> datas = new ArrayList<>();
		for (Integer pid : pids) {
			UserPermAssoc upa = new UserPermAssoc();
			upa.setUid(uid);
			upa.setPid(pid);
			datas.add(upa);
		}
		return this.saveBatch(datas);
	}

	@Override
	public boolean removeBatch(Long uid, List<Integer> pids) {
		LambdaQueryWrapper<UserPermAssoc> lqw = new LambdaQueryWrapper<>();
		lqw.eq(UserPermAssoc::getUid, uid).and(qw -> {
			for (Integer pid : pids) {
				qw.or().eq(UserPermAssoc::getPid, pid);
			}
		});
		return this.remove(lqw);
	}
}
