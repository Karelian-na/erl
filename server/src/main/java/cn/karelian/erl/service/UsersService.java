package cn.karelian.erl.service;

import cn.karelian.erl.DataTransferObject.AsignRoleParam;
import cn.karelian.erl.DataTransferObject.AuthorizeParam;
import cn.karelian.erl.DataTransferObject.VerifyCodeParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.RevisePasswordParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.entity.Roles;
import cn.karelian.erl.entity.UserPermAssoc;
import cn.karelian.erl.entity.UserRoleAssoc;
import cn.karelian.erl.entity.Usermsgs;
import cn.karelian.erl.entity.Users;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.mapper.RolesMapper;
import cn.karelian.erl.mapper.UserPermAssocMapper;
import cn.karelian.erl.mapper.UserRoleAssocMapper;
import cn.karelian.erl.mapper.UsermsgsMapper;
import cn.karelian.erl.mapper.UsersMapper;
import cn.karelian.erl.mapper.view.UsermsgsViewMapper;
import cn.karelian.erl.service.interfaces.IUsersService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.SpringContextUtil;
import cn.karelian.erl.utils.VerifyCodeCache;
import cn.karelian.erl.utils.Utils;
import cn.karelian.erl.utils.Utils.FileCategory;
import cn.karelian.erl.view.UsermsgsView;
import lombok.Getter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 管理用户的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
@Getter
public class UsersService extends ErlServiceImpl<UsersMapper, Users> implements IUsersService {
	public static final String BindVerifyUrl = "/Users/bind/verify/send";
	public static final String ReviseVerifyUrl = "/Users/revisepwd/verify/send";

	@Autowired
	private UsermsgsMapper usermsgsMapper;
	@Autowired
	private UsermsgsViewMapper viewMapper;
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private UserPermAssocMapper userPermAssocMapper;
	@Autowired
	private UserRoleAssocMapper userRoleAssocMapper;
	@Autowired
	private RolesMapper rolesMapper;

	private Result checkFields(Usermsgs usermsg, boolean add) {
		int code = EntityUtil.CheckStringField(usermsg.getName(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("name", code);
		}

		code = EntityUtil.CheckStringField(usermsg.getAvatar(), 5, 255, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("avatar", code);
		}

		code = EntityUtil.CheckStringField(usermsg.getEmail(), 5, 50, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("email", code);
		}

		code = EntityUtil.CheckStringField(usermsg.getPhone(), 11, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("phone", code);
		}

		code = EntityUtil.CheckNumberField(usermsg.getAge(), (byte) 10, (byte) 125, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("age", code);
		}

		code = EntityUtil.CheckNumberField(usermsg.getGender(), (byte) 1, (byte) 3, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("gender", code);
		}

		code = EntityUtil.CheckNumberField(usermsg.getClan(), (byte) 1, (byte) 56, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("clan", code);
		}

		code = EntityUtil.CheckNumberField(usermsg.getPolitical_status(), (byte) 1, (byte) 13, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("political_status", code);
		}

		code = EntityUtil.CheckStringField(usermsg.getProfile(), 1, 100, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("profile", code);
		}

		return null;
	}

	@Override
	public Result index(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<UsermsgsView> qw = Wrappers.query();
		qw.ne("id", LoginInfomationUtil.getUserId());
		return super.index(viewMapper, params, qw);
	}

	@Override
	public Result getPaperAuthor(Long id) {
		LambdaQueryWrapper<UsermsgsView> lqw = Wrappers.lambdaQuery();
		lqw.select(UsermsgsView::getName, UsermsgsView::getRoles)
				.eq(UsermsgsView::getId, id);
		UsermsgsView msgView = viewMapper.selectOne(lqw);
		Result result = new Result();
		result.setSuccess(msgView != null);
		if (result.isSuccess()) {
			result.setData(msgView);
		}
		return result;
	}

	@Override
	public Result selfindex() throws NullRequestException {
		Result result = new Result();
		UsermsgsView msg = viewMapper.selectById(LoginInfomationUtil.getUserId());
		result.setSuccess(msg != null);
		result.setData(msg);
		return result;
	}

	@Override
	public Result edit(Usermsgs usermsg) {
		Result result = this.checkFields(usermsg, false);
		if (result != null) {
			return result;
		}

		result = new Result();
		String avatarName = usermsg.getAvatar();
		if (!ObjectUtils.isEmpty(avatarName)) {
			String url = Utils.CopyTempFileToSpecifiedCategory(FileCategory.IMAGE, avatarName);
			if (url == null) {
				result.setMsg("头像保存失败!");
				return result;
			}
			usermsg.setAvatar(url);
		}

		result.setSuccess(usermsgsMapper.updateById(usermsg) == 1);
		return result;
	}

	@Override
	public Result selfedit(Usermsgs usermsg) throws NullRequestException {
		usermsg.setId(LoginInfomationUtil.getUserId());
		return this.edit(usermsg);
	}

	@Override
	@Transactional(rollbackFor = TransactionFailedException.class)
	public Result authorize(AuthorizeParam params) throws TransactionFailedException {
		if (params.id == null || params.auths.size() == 0) {
			return Result.paramError(null);
		}

		Boolean success;
		for (Integer pid : params.auths.keySet()) {
			success = false;
			Byte auth = params.auths.get(pid);
			switch (auth) {
				case 0:
				case 1:
					UserPermAssoc upa = new UserPermAssoc();
					upa.setUid(params.id);
					upa.setPid(pid);
					success = userPermAssocMapper.insertOrUpdateByUnionKey(params.id, pid, auth);
					break;
				case 2:
					success = userPermAssocMapper.deleteByUnionKey(params.id, pid);
					break;
				default:
					break;
			}

			if (!success) {
				throw new TransactionFailedException(null);
			}
		}

		return new Result(true);
	}

	@Override
	public Result reset(List<Long> uids) {
		String hashPwd = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update("123456".getBytes());
			hashPwd = HexUtils.toHexString(messageDigest.digest());
		} catch (Exception e) {
			return Result.internalError(null);
		}

		LambdaUpdateWrapper<Users> luw = new LambdaUpdateWrapper<>();
		luw.set(Users::getPwd, hashPwd);
		luw.in(Users::getId, uids);

		return new Result(super.update(luw));
	}

	@Override
	public Result selfreset(RevisePasswordParam params) throws NullRequestException {
		int code = EntityUtil.CheckStringField(params.pwd, 64, true);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("pwd", code);
		}

		HttpSession session = LoginInfomationUtil.getSession();
		if (params.account == null && (params.account = String.valueOf(session.getAttribute("id"))) == null) {
			return Result.fieldError("account", FieldError.EMPTY);
		}

		Result result = new Result();
		VerifyCodeCache cache = LoginInfomationUtil.getVerifyCodeCache();
		boolean isVerified = cache.lastVerifiedUrl.equals(UsersService.ReviseVerifyUrl);

		if (params.old == null && !isVerified) {
			result.setMsg("未验证身份!");
			return result;
		}

		LambdaUpdateWrapper<Users> luw = new LambdaUpdateWrapper<>();
		luw.eq(Users::getId, params.account).func(t -> {
			if (!isVerified) {
				t.eq(Users::getPwd, params.old)
						.isNull(Users::getBind_email)
						.isNull(Users::getBind_phone);
			}
		}).set(Users::getPwd, params.pwd);

		result.setSuccess(super.update(luw));
		if (result.isSuccess()) {
			result.setCode(StatusCode.ERROR_UN_LOGIN);
			session.invalidate();
		}

		return result;
	}

	@Override
	public Result asignindex(List<Long> ids) {
		LambdaQueryWrapper<UserRoleAssoc> ulqw = Wrappers.lambdaQuery();
		ulqw.select(UserRoleAssoc::getRid)
				.in(UserRoleAssoc::getUid, ids)
				.groupBy(UserRoleAssoc::getRid)
				.having("COUNT(DISTINCT uid) = {0}", ids.size());

		Map<String, Object> data = new HashMap<>();
		data.put("common", userRoleAssocMapper.selectObjs(ulqw));

		LambdaQueryWrapper<Roles> lqw = Wrappers.lambdaQuery();
		lqw.select(Roles::getId, Roles::getName);
		data.put("roles", rolesMapper.selectList(lqw));

		Result result = new Result(true, data);
		return result;
	}

	@Override
	@Transactional(rollbackFor = TransactionFailedException.class)
	public Result asign(AsignRoleParam params) throws NullRequestException, TransactionFailedException {

		// Long uid = ((Integer) data.get("uid")).longValue();
		// Map<Byte, Byte> auths = ((Map<String, Object>)
		// data.get("auths")).entrySet().stream().map(
		// entry -> new AbstractMap.SimpleEntry<Byte, Byte>(
		// Byte.valueOf(entry.getKey()),
		// ((Integer) entry.getValue()).byteValue()))
		// .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		List<Byte> assignRids = new ArrayList<>();
		List<Byte> deAssignedRids = new ArrayList<>();
		for (Byte rid : params.auths.keySet()) {
			Byte auth = params.auths.get(rid);
			switch (auth) {
				case 0:
					deAssignedRids.add(rid);
					break;
				case 1:
					assignRids.add(rid);
					break;
				default:
					break;
			}
		}
		if (deAssignedRids.size() != 0) {
			LambdaQueryWrapper<UserRoleAssoc> lqw = Wrappers.lambdaQuery();
			lqw.in(UserRoleAssoc::getRid, deAssignedRids)
					.in(UserRoleAssoc::getUid, params.ids);
			if (userRoleAssocMapper.delete(lqw) == 0) {
				throw new TransactionFailedException();
			}
		}

		if (assignRids.size() != 0) {
			if (!userRoleAssocMapper.insertBatchByUnionKey(Map.of("uids", params.ids, "rids", assignRids))) {
				throw new TransactionFailedException();
			}
		}
		return new Result(true);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result authorizeindex(Long id, Boolean all) throws NullRequestException {
		AuthorizeData authorizeData = new AuthorizeData();
		Long loginedId = LoginInfomationUtil.getUserId();
		if (loginedId.equals(id)) {
			return Result.paramError("id", "不能授权自己!");
		}

		if (all) {
			List<Map<String, Object>> allPermissions = permissionsService.getAuthorizedPermissions(loginedId);
			authorizeData.all = allPermissions;
		}

		List<Integer> authPermis = permissionsService.getAuthorizedPermissions(id)
				.stream().map(permi -> (Integer) permi.get("id")).collect(Collectors.toList());
		authorizeData.auth = authPermis;
		LambdaQueryWrapper<UserPermAssoc> lqw = new LambdaQueryWrapper<>();
		lqw.select(UserPermAssoc::getPid).eq(UserPermAssoc::getUid, id);

		List<Integer> independentPermis = List.class.cast(userPermAssocMapper.selectObjs(lqw));
		authorizeData.independent = independentPermis;

		return new Result(true, authorizeData);
	}

	@Override
	public Result bind(VerifyCodeParam params) throws NullRequestException {
		VerifyCodeCache cache = LoginInfomationUtil.getVerifyCodeCache();

		Result result = null;
		boolean lastStepVerified = cache.lastVerifiedUrl.equals(UsersService.BindVerifyUrl);

		if (params.code == null && params.serial == null) {
			if (!lastStepVerified) {
				return new Result("未验证身份!");
			}
			result = new Result(true);
		}

		GeneralService generalService = SpringContextUtil.getBean(GeneralService.class);
		if (result == null && !(result = generalService.verify(params)).isSuccess()) {
			return result;
		}

		LambdaUpdateWrapper<Users> luw = new LambdaUpdateWrapper<>();
		luw.eq(Users::getId, LoginInfomationUtil.getUserId());
		switch (params.type) {
			case Email:
				luw.isNull(!lastStepVerified, Users::getBind_email);
				luw.set(Users::getBind_email, params.serial);
				break;
			case Phone:
				luw.isNull(!lastStepVerified, Users::getBind_phone);
				luw.set(Users::getBind_phone, params.serial);
				break;
		}

		result.setSuccess(super.update(luw));
		cache.clear();
		return result;
	}

	@Override
	public Result getverifies(String account) throws NullRequestException {
		if (account == null && (account = String.valueOf(LoginInfomationUtil.getUserId())) == null) {
			return Result.fieldError("account", FieldError.EMPTY);
		}

		Result result = new Result();
		LambdaQueryWrapper<UsermsgsView> lqw = new LambdaQueryWrapper<>();
		lqw.select(UsermsgsView::getId, UsermsgsView::getBind_email, UsermsgsView::getBind_phone)
				.eq(UsermsgsView::getId, account);

		UsermsgsView usermsg = viewMapper.selectOne(lqw);
		if (usermsg == null) {
			result.setMsg("未找到该账户!");
			return result;
		}

		usermsg.setId(null);
		result.setSuccess(true);
		result.setData(EntityUtil.ToMap(usermsg));
		return result;
	}
}

class AuthorizeData {
	public List<Map<String, Object>> all;
	public List<Integer> auth;
	public List<Integer> independent;
}