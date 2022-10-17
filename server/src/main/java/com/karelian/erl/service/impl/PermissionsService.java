/*
 * @Author: Karelian_na
 */
/*
 * @Author: Karelian_na
 */
package com.karelian.erl.service.impl;

import com.karelian.erl.Exceptions.NullRequestException;
import com.karelian.erl.Exceptions.UnLoginException;
import com.karelian.erl.entity.Permissions;
import com.karelian.erl.mapper.PermissionsMapper;
import com.karelian.erl.service.IPermissionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.*;

/**
 * <p>
 * 管理权限目录的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class PermissionsService extends ServiceImpl<PermissionsMapper, Permissions> implements IPermissionsService {

	@Autowired
	private UserPermAssocService userPermAssocService;
	@Autowired
	private UserRoleAssocService userRoleAssocService;
	@Autowired
	private RolePermAssocService rolePermAssocService;

	@Override
	public Permissions getByUrl(String url) {
		LambdaQueryWrapper<Permissions> lqw = new LambdaQueryWrapper<Permissions>();
		lqw.eq(Permissions::getUrl, url);
		return this.baseMapper.selectOne(lqw);
	}

	@Override
	public Byte getTypeById(Integer id) {
		LambdaQueryWrapper<Permissions> lqw = new LambdaQueryWrapper<Permissions>();
		lqw.select(Permissions.class, i -> i.getProperty().equals("type")).eq(Permissions::getType, id);
		List<Object> objs = this.baseMapper.selectObjs(lqw);
		if (objs.size() != 0) {
			return (Byte) objs.get(0);
		}
		return null;
	}

	@Override
	public Integer getPidById(Integer id) {
		LambdaQueryWrapper<Permissions> lqw = new LambdaQueryWrapper<Permissions>();
		lqw.select(Permissions.class, i -> i.getProperty().equals("pid")).eq(Permissions::getType, id);
		List<Object> objs = this.baseMapper.selectObjs(lqw);
		if (objs.size() != 0) {
			return (Integer) objs.get(0);
		}
		return null;
	}

	@Override
	public boolean checkTypeAssoc(Byte pType, Byte curType) {
		switch (pType) {
			default:
			case Permissions.TYPE_MENU:
				// if (curType == Permissions.TYPE_MENU || curType == Permissions.TYPE_ITEM) {
				if (curType == Permissions.TYPE_MENU || curType == Permissions.TYPE_ITEM
						|| curType == Permissions.TYPE_OPER) { /// change
					return true;
				}
				break;
			case Permissions.TYPE_ITEM:
				if (curType == Permissions.TYPE_PAGE || curType == Permissions.TYPE_OPER) {
					return true;
				}
				break;
			case Permissions.TYPE_PAGE:
				if (curType == Permissions.TYPE_OPER) {
					return true;
				}
				break;
		}
		return false;
	}

	@Override
	public boolean isAuthorized(Integer id) throws NullRequestException, UnLoginException {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession();

		Long uid = (Long) session.getAttribute("id");
		if (null == uid) {
			throw new UnLoginException();
		}

		Boolean authorize = userPermAssocService.isAuthorized(id, uid);
		if (null != authorize) {
			return authorize;
		}

		List<Byte> roles = userRoleAssocService.getRoles(uid);
		for (Byte rid : roles) {
			if (rolePermAssocService.isAuthorized(id, rid)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean hasCircularRelationship(Integer id, Integer pid) {
		while (pid != null) {
			pid = this.getPidById(pid);
			if (pid == id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Byte getLevel(Integer pid) {
		Byte level = 1;

		if (pid != null) {
			while ((pid = this.getPidById(pid)) != null) {
				++level;
			}
		}
		return level;
	}
}
