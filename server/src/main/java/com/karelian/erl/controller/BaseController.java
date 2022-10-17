/*
 * @Author: Karelian_na
 */
package com.karelian.erl.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.context.request.*;

import com.karelian.erl.Exceptions.NullRequestException;
import com.karelian.erl.Exceptions.PermissionNotFoundException;
import com.karelian.erl.Exceptions.UnLoginException;
import com.karelian.erl.entity.Logs;
import com.karelian.erl.entity.Permissions;
import com.karelian.erl.service.impl.LogsService;
import com.karelian.erl.service.impl.PermissionsService;

public class BaseController {

	@Autowired
	private LogsService logsService;
	@Autowired
	private PermissionsService permissionsService;

	protected boolean log() throws NullRequestException {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		HttpServletRequest request = ((ServletRequestAttributes)attributes).getRequest();
		HttpSession session = request.getSession();
		Logs log = new Logs();
		log.setUid((long)session.getAttribute("id"));
		log.setType(request.getMethod());
		log.setUrl(request.getRequestURI());
		log.setParams(JSONObject.wrap(request.getParameterMap()).toString());
		log.setIp(request.getRemoteAddr());
		return logsService.save(log);
	}

	protected boolean canAccess() throws NullRequestException, PermissionNotFoundException, UnLoginException {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		HttpServletRequest request = ((ServletRequestAttributes)attributes).getRequest();
		String url = request.getRequestURI();
		
		Permissions permission = permissionsService.getByUrl(url);
		if (null == permission) {
			throw new PermissionNotFoundException();
		}

		if (permissionsService.isAuthorized(permission.getId())) {
			this.log();
			return true;
		}
		return false;
	}
}
