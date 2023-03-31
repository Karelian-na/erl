/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Permissions;
import cn.karelian.erl.mapper.PermissionsMapper;
import cn.karelian.erl.mapper.UserPermAssocMapper;
import cn.karelian.erl.mapper.UserRoleAssocMapper;
import cn.karelian.erl.service.interfaces.IPermissionsService;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.PageData;
import cn.karelian.erl.utils.WebPageInfo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
public class PermissionsService extends ErlServiceImpl<PermissionsMapper, Permissions>
		implements IPermissionsService {
	@Autowired
	private UserPermAssocMapper userPermAssocMapper;
	@Autowired
	private UserRoleAssocMapper userRoleAssocMapper;
	@Autowired
	private RolePermAssocService rolePermAssocService;

	private static Consumer<Permissions> func = null;

	@Override
	public Result index(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<Permissions> qw = Wrappers.query();
		qw.orderByAsc("pid").orderByAsc("type");

		PageData<Permissions> pageData = new PageData<>();
		if (params.initPageSize == null) {
			pageData.data = baseMapper.selectList(qw);
			return new Result(true, pageData);
		} else {
			WebPageInfo<Permissions> info = super.getWebPageInfo(Permissions.class, qw);
			info.pageData = pageData;
			info.pageData.data = baseMapper.selectList(qw);
			return new Result(true, info);
		}
	}

	@Override
	public Permissions getByUrl(String url) {
		return super.baseMapper.getByUrl(url);
	}

	@Override
	public Byte getTypeById(Integer id) {
		LambdaQueryWrapper<Permissions> lqw = new LambdaQueryWrapper<Permissions>();
		lqw.select(Permissions::getType).eq(Permissions::getId, id);
		List<Object> objs = this.baseMapper.selectObjs(lqw);
		if (objs.size() != 0) {
			return (byte) ((Integer) objs.get(0)).intValue();
		}
		return null;
	}

	@Override
	public Integer getPidById(Integer id) {
		LambdaQueryWrapper<Permissions> lqw = new LambdaQueryWrapper<Permissions>();
		lqw.select(Permissions::getPid).eq(Permissions::getId, id);
		List<Object> objs = this.baseMapper.selectObjs(lqw);
		if (objs.size() != 0) {
			return (Integer) objs.get(0);
		}
		return null;
	}

	@Override
	public boolean checkTypeAssoc(Byte pType, Byte curType) {
		switch (pType) {
			case 0:
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
			default:
				return false;
		}
		return false;
	}

	@Override
	public boolean isAuthorized(Permissions permission) throws NullRequestException {
		if (!permission.getStatus()) {
			return false;
		}

		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}

		Integer id = permission.getId();
		Long uid = LoginInfomationUtil.getUserId();
		Boolean authorize = userPermAssocMapper.isAuthorized(id, uid);
		if (null != authorize) {
			return authorize;
		}

		List<Byte> roles = userRoleAssocMapper.getRoles(uid);
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

	@Override
	public List<Map<String, Object>> getAuthorizedPermissions(Long uid) {
		return this.baseMapper.getAuthorizedPermissions(uid);
	}

	@Override
	public List<Permissions> getAuthorizedMenu(Long uid) {
		return this.baseMapper.getAuthorizedMenu(uid);
	}

	@Override
	public List<Permissions> getAuthorizedBrotherPages(Long uid, Integer pid) {
		return this.baseMapper.getAuthorizedBrotherPages(uid, pid);
	}

	@Override
	public List<Permissions> getAuthorizedOperationPermissions(Long uid, Permissions permission) {
		return this.baseMapper.getAuthorizedOperationPermissions(uid, permission);
	}

	@Override
	public Result update(Permissions permission) {
		Result result = new Result();
		Permissions oldPermi = this.getById(permission.getId());
		if (null == oldPermi) {
			result.setMsg("权限不存在!");
			return result;
		}

		Byte curType = permission.getType() != null ? permission.getType() : oldPermi.getType();
		Integer pid = permission.getPid() == null ? this.getPidById(oldPermi.getId())
				: permission.getPid();
		Byte pType = pid != null ? this.getTypeById(pid) : 0;

		result.setSuccess(this.checkTypeAssoc(pType, curType));
		if (!result.isSuccess()) {
			result.setMsg("权限类型关联错误!");
			return result;
		}

		result.setSuccess(!this.hasCircularRelationship(permission.getId(), pid));
		if (!result.isSuccess()) {
			result.setMsg("父子权限关联错误!");
			return result;
		}

		if (permission.getStatus() != null) {
			LambdaQueryWrapper<Permissions> qw = new LambdaQueryWrapper<>();
			qw.select(Permissions::getId, Permissions::getPid)
					.eq(Permissions::getPid, permission.getId());

			List<Permissions> revising = new ArrayList<>();
			revising.add(permission);

			func = (Permissions child) -> {
				child.setStatus(permission.getStatus());
				revising.add(child);

				qw.clear();
				qw.select(Permissions::getId, Permissions::getPid)
						.eq(Permissions::getPid, child.getId());

				List<Permissions> children = this.list(qw);

				if (children.size() != 0) {
					children.forEach(func);
				}
			};
			this.list(qw).forEach(func);

			Permissions temp = permission;
			if (permission.getStatus()) {
				while (true) {
					qw.clear();
					qw.select(Permissions::getId, Permissions::getPid, Permissions::getStatus)
							.eq(Permissions::getId, temp.getPid());

					temp = this.getOne(qw);
					if (temp == null || temp.getStatus()) {
						break;
					}
					temp.setStatus(true);
					revising.add(temp);
				}
			}

			result.setSuccess(this.updateBatchById(revising));
		} else {
			result.setSuccess(this.updateById(permission));
		}
		return result;
	}

	@Override
	public Result add(Permissions permission) {
		Result result = new Result();

		Integer id = permission.getId();
		long count = this.count(this.lambdaQuery().getWrapper().eq(Permissions::getId, id));
		if (count == 1) {
			result.setMsg("权限已存在!");
			return result;
		}

		Byte type = permission.getType();
		Byte pType = this.getTypeById(permission.getPid());
		result.setSuccess(this.checkTypeAssoc(pType, type));
		if (!result.isSuccess()) {
			result.setMsg("权限关联错误!");
		}

		result.setSuccess(this.save(permission));
		if (result.isSuccess()) {
			result.setData(permission);
		}
		return result;

	}

	@Override
	public List<Map<String, Object>> getAuthorizablePermissions() {
		return baseMapper.getAuthorizablePermissions();
	}
}
