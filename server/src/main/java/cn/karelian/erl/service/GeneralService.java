package cn.karelian.erl.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;

import cn.karelian.erl.DataTransferObject.VerifyCodeParam;
import cn.karelian.erl.DataTransferObject.VerifyCodeSendParam;
import cn.karelian.erl.DataTransferObject.LoginParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.ErlApplication;
import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.entity.Permissions;
import cn.karelian.erl.entity.Users;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.service.interfaces.IGeneralService;
import cn.karelian.erl.utils.VerifyCodeCache;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.Utils;
import cn.karelian.erl.view.UsermsgsView;

@Service
public class GeneralService implements IGeneralService {
	@Autowired
	private UsersService usersService;
	@Autowired
	private UsersService usersServices;
	@Autowired
	private PermissionsService permissionsService;

	@Autowired
	private MailService mailService;
	@Autowired
	private SMSService smsService;

	@Override
	public Result login(LoginParam params) {
		int code = EntityUtil.CheckStringField(params.account, 6, 50, true);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("account", code);
		}

		code = EntityUtil.CheckStringField(params.pwd, 64, true);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("pwd", code);
		}

		Result result = new Result();
		HttpServletRequest request;
		try {
			request = Utils.getRequest();
		} catch (Exception e) {
			result.setMsg("非法的空请求!");
			return result;
		}
		HttpSession session = request.getSession();

		LambdaQueryChainWrapper<Users> lqcw = usersService.lambdaQuery()
				.select(Users::getId, Users::getPwd);
		if (params.account.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
			lqcw.eq(Users::getBind_email, params.account);
		} else if (params.account.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
			lqcw.eq(Users::getBind_phone, params.account);
		} else if (params.account.matches("^\\d{6}|\\d{8}|$\\d{12}$")) {
			lqcw.eq(Users::getId, params.account);
		} else {
			return Result.fieldError("account", FieldError.FORMAT);
		}

		Users user = lqcw.one();
		if (null == user) {
			result.setMsg("账号不存在!");
			return result;
		}

		if (!params.pwd.equals(user.getPwd())) {
			result.setMsg("密码错误!");
			return result;
		}

		if (session.getAttribute("id") != null) {
			result.setSuccess(true);
			result.setMsg("您已登陆, 无需重复登陆!");
			return result;
		}

		LambdaQueryWrapper<UsermsgsView> lqw = new LambdaQueryWrapper<>();
		lqw.select(UsermsgsView::getName)
				.eq(UsermsgsView::getId, user.getId());
		UsermsgsView userMsg = usersServices.getViewMapper().selectOne(lqw);

		session.setAttribute("id", user.getId());
		session.setAttribute("name", userMsg.getName());

		if (params.remember) {
			session.setMaxInactiveInterval(3600 * 24 * 7);
		}

		user.setPwd(null);
		user.setLast_login_ip(request.getRemoteAddr());
		user.setLast_login_time(LocalDateTime.now());

		result.setSuccess(usersService.updateById(user));
		return result;
	}

	@Override
	public void logout() {
		try {
			HttpSession session = LoginInfomationUtil.getSession();
			session.invalidate();
		} catch (Exception e) {
		}
	}

	@Override
	public Result index() throws NullRequestException {
		HttpServletRequest request = Utils.getRequest();
		request.changeSessionId();
		Long uid = (Long) request.getSession().getAttribute("id");

		IndexInfo info = new IndexInfo();
		info.menus = permissionsService.getAuthorizedMenu(uid);

		LambdaQueryWrapper<UsermsgsView> lqw = new LambdaQueryWrapper<>();
		lqw.select(UsermsgsView::getName, UsermsgsView::getAvatar);
		lqw.eq(UsermsgsView::getId, uid);
		info.userMsg = usersServices.getViewMapper().selectMaps(lqw).get(0);
		return new Result(true, info);
	}

	@Override
	public Result upload(MultipartFile[] files) {
		MessageDigest digest;
		List<String> paths = new ArrayList<>();

		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			return Result.internalError("获取实例失败!");
		}

		for (MultipartFile file : files) {
			try {
				digest.update(file.getBytes());
				digest.update(Utils.getRequest().getRequestURI().getBytes());
			} catch (Exception e) {
				return Result.internalError("实例计算失败!");
			}

			String originName = file.getOriginalFilename();
			String[] originNamesInfo;
			if (originName == null || (originNamesInfo = originName.split("\\.")).length != 2) {
				return Result.internalError("文件名不合法!");
			}
			String fileName = HexUtils.toHexString(digest.digest()) + "." + originNamesInfo[1];

			Path temp = Path.of(ErlApplication.TempPath, fileName);
			if (!Files.exists(temp)) {
				try {
					file.transferTo(temp);
				} catch (Exception e) {
					continue;
				}
			}
			paths.add(fileName);
		}
		if (paths.size() == 0) {
			return new Result(false);
		}

		return new Result(true, String.join(";", paths));

	}

	@Override
	public Result send(VerifyCodeSendParam params) throws NullRequestException {
		Result result = new Result();
		String requestUrl = Utils.getRequest().getRequestURI();

		if (requestUrl.contains("verify")) {
			LambdaQueryWrapper<Users> ulqw = new LambdaQueryWrapper<>();
			ulqw.eq(Users::getId, LoginInfomationUtil.getUserId());
			switch (params.type) {
				case Email:
					ulqw.select(Users::getBind_email);
					break;
				case Phone:
					ulqw.select(Users::getBind_phone);
					break;
				default:
					break;
			}
			String serial = usersService.getObj(ulqw, t -> (String) t);
			if (serial == null || !serial.equals(params.serial)) {
				result.setMsg("账户验证失败!");
				return result;
			}
		}

		int code = EntityUtil.CheckStringField(params.pageTraceId, 64, true);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("pageTraceId", code);
		}

		code = EntityUtil.CheckStringField(params.serial, 5, 50, true);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("serial", code);
		}

		if (params.type == null) {
			return Result.fieldError("type", FieldError.EMPTY);
		}

		VerifyCodeCache cache = LoginInfomationUtil.getVerifyCodeCache();
		switch (params.type) {
			case Email:
				if (!params.serial.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
					return Result.fieldError("serial", FieldError.FORMAT);
				}
				break;
			case Phone:
				if (!params.serial.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
					return Result.fieldError("serial", FieldError.FORMAT);
				}
				break;
			default:
				return Result.fieldError("serial", FieldError.FORMAT);
		}

		LocalDateTime now = LocalDateTime.now();
		if (params.serial.equals(cache.serial) && params.pageTraceId.equals(cache.pageTraceId) &&
				cache.lastSendTime.plusMinutes(1L).isAfter(now)) {
			result.setMsg("验证码已经发送! 如果您未接受到验证码, 请在" + now.compareTo(cache.lastSendTime) + "s后重试!");
			return result;
		}

		cache.serial = params.serial;
		switch (params.type) {
			case Email:
				result.setSuccess(mailService.sendVerifyCode(cache));
				break;
			case Phone:
				result.setSuccess(smsService.sendVerifyCode(cache));
				break;
			default:
				break;
		}

		if (!result.isSuccess()) {
			result.setData("验证码发送失败!");
			return result;
		}

		cache.lastSendType = params.type;
		cache.lastSendTime = LocalDateTime.now();
		cache.lastSendUrl = Utils.getRequest().getRequestURI();
		cache.pageTraceId = params.pageTraceId;
		return result;
	}

	@Override
	public Result verify(VerifyCodeParam params) {
		Result result = new Result();
		VerifyCodeCache cache = LoginInfomationUtil.getVerifyCodeCache();
		if (params.pageTraceId == null || !params.pageTraceId.equals(cache.pageTraceId)) {
			result.setMsg("验证码已过期!");
			return result;
		}

		if (params.serial == null || !params.serial.equals(cache.serial)) {
			result.setMsg("验证账户不一致! 请确认账户!");
			return result;
		}

		if (params.code == null || !params.code.equals(cache.code)) {
			result.setMsg("验证码错误!");
			return result;
		}

		cache.lastVerifiedUrl = cache.lastSendUrl;
		result.setSuccess(true);
		return result;
	}
}

class IndexInfo {
	public List<Permissions> menus;
	public Map<String, Object> userMsg;
}
