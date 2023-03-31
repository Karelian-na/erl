package cn.karelian.erl.config;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Exceptions.UnAuthorizedAccessException;
import cn.karelian.erl.Exceptions.UnLoginException;
import cn.karelian.erl.annotation.Log;
import cn.karelian.erl.entity.Logs;
import cn.karelian.erl.entity.Permissions;
import cn.karelian.erl.service.LogsService;
import cn.karelian.erl.service.PermissionsService;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.Utils;

@Aspect
@Component
public class AuthorizeAndLogConfig {
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private LogsService logsService;
	@Autowired
	private ObjectMapper objectMapper;

	@Pointcut("@annotation(cn.karelian.erl.annotation.Log)")
	private void logPointcutFilter() {

	}

	@Pointcut("@annotation(cn.karelian.erl.annotation.Authorize)")
	private void authorizePointcutFilter() {

	}

	@Around("authorizePointcutFilter()")
	public Object canAccess(ProceedingJoinPoint proceedingJoinPoint) throws NullRequestException, UnLoginException,
			PermissionNotFoundException, UnAuthorizedAccessException, Throwable {
		HttpServletRequest request = Utils.getRequest();
		if (LoginInfomationUtil.getUserId() == null) {
			throw new UnLoginException();
		}

		String url = request.getRequestURI();
		Permissions permission = permissionsService.getByUrl(url);
		if (null == permission) {
			permission = permissionsService.getByUrl(url.replaceFirst("(.*)/.*/([a-zA-Z]*)$", "$1/$2"));
			if (null == permission) {
				throw new PermissionNotFoundException();
			}
		}

		if (!permissionsService.isAuthorized(permission)) {
			throw new UnAuthorizedAccessException();
		}

		this.log(request, permission.getName(), proceedingJoinPoint);
		return proceedingJoinPoint.proceed();
	}

	private void log(HttpServletRequest request, String name, JoinPoint joinPoint) {
		String param = null;
		Logs log = new Logs();
		log.setUid(LoginInfomationUtil.getUserId());
		log.setType(request.getMethod());
		log.setUrl(request.getRequestURI());
		try {
			if (request.getMethod().equals("GET") || request.getMethod().equals("DELETE")) {
				param = objectMapper.writeValueAsString(request.getParameterMap());
			} else {
				param = objectMapper.writeValueAsString(joinPoint.getArgs());
			}
		} catch (Exception e) {
			param = "error occured";
		}
		log.setParams(param);
		log.setIp(request.getRemoteAddr());
		log.setTitle(name);
		logsService.save(log);
	}

	@Around("logPointcutFilter() && @annotation(log)")
	public Object log(ProceedingJoinPoint joinPoint, Log log) throws NullRequestException, UnLoginException, Throwable {
		HttpServletRequest request = Utils.getRequest();
		if (log.checkLogin()) {
			if (request.getSession().getAttribute("id") == null) {
				throw new UnLoginException();
			}
		}
		this.log(request, log.value(), joinPoint);

		return joinPoint.proceed();
	}
}
