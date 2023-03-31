package cn.karelian.erl.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.karelian.erl.Exceptions.NullRequestException;

public class LoginInfomationUtil {

	public static String getUserName() {

		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			return null;
		}
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		return (String) request.getSession().getAttribute("name");
	}

	public static Long getUserId() {

		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			return null;
		}
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		return (Long) request.getSession().getAttribute("id");
	}

	public static VerifyCodeCache getVerifyCodeCache() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			return new VerifyCodeCache();
		}
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession();
		VerifyCodeCache cache = (VerifyCodeCache) session.getAttribute("verifyCodeCache");
		if (cache == null) {
			cache = new VerifyCodeCache();
			session.setAttribute("verifyCodeCache", cache);
		}
		return cache;
	}

	public static HttpSession getSession() throws NullRequestException {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		return request.getSession();
	}
}
