package cn.karelian.erl.service;

import cn.karelian.erl.DataTransferObject.AuthorizeParam;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.entity.Roles;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.mapper.RolePermAssocMapper;
import cn.karelian.erl.mapper.RolesMapper;
import cn.karelian.erl.service.interfaces.IRolesService;
import cn.karelian.erl.utils.EntityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 管理角色的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-02-23
 */
@Service
public class RolesService extends ErlServiceImpl<RolesMapper, Roles> implements IRolesService {
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private RolePermAssocMapper rolePermAssocMapper;

	private Result checkFields(Roles role, boolean add) {
		int code = EntityUtil.CheckStringField(role.getName(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("name", code);
		}

		code = EntityUtil.CheckNumberField(role.getLevel(), (byte) 2, (byte) 100, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("level", code);
		}

		code = EntityUtil.CheckStringField(role.getDescrip(), 0, 100, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("descrip", code);
		}

		return null;
	}

	@Override
	public Result add(Roles role) {
		Result result = this.checkFields(role, true);
		if (result != null) {
			return result;
		}

		role.setUpdate_time(null);

		result = new Result(super.save(role));
		if (result.isSuccess()) {
			result.setData(EntityUtil.ToMap(role));
		}

		return result;
	}

	@Override
	public Result edit(Roles role) {
		if (role.getId() == null) {
			return Result.fieldError("id", FieldError.EMPTY);
		}

		Result result = this.checkFields(role, false);
		if (result != null) {
			return result;
		}

		result = new Result();
		if (role.getId() <= 4 && (role.getName() != null || role.getLevel() != null)) {
			result.setMsg("不能修改给定字段!");
			return result;
		}

		role.setAdd_time(null);
		role.setAdd_user(null);

		result.setSuccess(super.updateById(role));
		if (result.isSuccess()) {
			result.setData(EntityUtil.ToMap(role));
		}
		return result;
	}

	@Override
	public Result authorizeindex(Integer id, Boolean all) {
		Result result = new Result();
		if (id == 1) {
			result.setMsg("不能授权此角色!");
			return result;
		}

		if (!super.lambdaQuery().eq(Roles::getId, id).exists()) {
			result.setMsg("角色不存在!");
			return result;
		}

		AuthorizeData authorizeData = new AuthorizeData();
		authorizeData.auth = rolePermAssocMapper.getAuthorizedPermissionIds(id);
		if (all) {
			authorizeData.all = permissionsService.getAuthorizablePermissions();
		}

		return new Result(true, authorizeData);
	}

	@Override
	@Transactional(rollbackFor = TransactionFailedException.class)
	public Result authorize(AuthorizeParam params) throws TransactionFailedException {
		int code = EntityUtil.CheckNumberField(params.id, 2L, 100L, true);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("id", code);
		}

		if (params.auths.size() == 0) {
			return Result.fieldError("auths", FieldError.EMPTY);
		}

		for (Integer pid : params.auths.keySet()) {
			Byte auth = params.auths.get(pid);
			switch (auth) {
				case 1:
					if (!rolePermAssocMapper.insertByUnionKey(params.id.byteValue(), pid)) {
						throw new TransactionFailedException();
					}
					break;
				case 2:
					if (!rolePermAssocMapper.deleteByUnionKey(params.id.byteValue(), pid)) {
						throw new TransactionFailedException();
					}
					break;
				default:
					break;
			}
		}

		return new Result(true);
	}

	@Override
	public Result delete(Integer id) {
		Result result = new Result();
		if (id <= 2) {
			result.setMsg("不能删除该角色!");
			return result;
		}

		return new Result(super.removeById(id));
	}
}
