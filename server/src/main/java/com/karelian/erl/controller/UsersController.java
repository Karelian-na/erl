/*
 * @Author: Karelian_na
 */
package com.karelian.erl.controller;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.servlet.http.*;

import org.apache.ibatis.session.SqlSession;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;

import com.karelian.erl.ErlApplication;
import com.karelian.erl.Result;
import com.karelian.erl.Exceptions.*;
import com.karelian.erl.entity.UserPermAssoc;
import com.karelian.erl.entity.Users;
import com.karelian.erl.mapper.UserPermAssocMapper;
import com.karelian.erl.service.impl.UsersService;


/**
 * <p>
 * 管理用户的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/users")
public class UsersController extends BaseController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private UserRoleAssocService userRoleAssocService;

	@DeleteMapping("/delete")
	public Result deleteUser(@RequestParam long id) throws Exception {
		if (super.canAccess()) {
			boolean success = usersService.removeById(id);
			return new Result(success);
		}
		return Result.PermissionNotAllowed;
	}

	@PostMapping("/login")
	public Result login(@RequestParam("name") long id, @RequestParam("pwd") String pwd) throws NullRequestException {
		Result result = new Result(false);
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession();

		Users user = usersService.getById(id);
		if (null == user) {
			result.setMsg("账号不存在!");
			return result;
		}

		if (!user.getPwd().equals(pwd)) {
			result.setMsg("密码错误!");
			return result;
		}

		if (null != session.getAttribute("id")) {
			result.setSuccess(true);
			result.setMsg("您已登陆, 无需重复登陆!");
			return result;
		}
		session.setAttribute("id", id);

		if (user.getLastLoginSession() != session.getId()) {
			user.setLastLoginSession(session.getId());
			user.setLastLoginIp(request.getRemoteAddr());
		}
		user.setLastLoginDate(LocalDateTime.now());
		usersService.updateById(user);

		result.setSuccess(super.log());
		return result;
	}

	@PostMapping("/logout")
	public void logout() throws NullRequestException {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession();
		session.invalidate();
	}

	@PostMapping("/authorize")
	public Result authorize(@RequestParam("uid") Long uid, @RequestBody HashMap<Integer, Byte> auths)
			throws Exception {
		if (super.canAccess()) {
			SqlSession sqlSession = null;
			try {
				sqlSession = ErlApplication.getSqlSession();
				sqlSession.getConnection().setAutoCommit(false);
				UserPermAssocMapper userPermAssocMapper = sqlSession.getMapper(UserPermAssocMapper.class);

				for (Integer pid : auths.keySet()) {
					Byte auth = auths.get(pid);
					switch (auth) {
						case 0:
						case 1:
							UserPermAssoc upa = new UserPermAssoc();
							upa.setUid(uid);
							upa.setPid(pid);
							userPermAssocMapper.insertOrUpdateByUnionKey(uid, pid, auth);
							break;
						case 2:
							userPermAssocMapper.deleteByUnionKey(uid, pid);
							break;
						default:
							break;
					}
				}
				sqlSession.commit();
				return new Result(true);
			} catch (Exception e) {
				if (sqlSession != null) {
					sqlSession.rollback();
				}
				throw e;
			} finally {
				if (null != sqlSession) {
					sqlSession.close();
				}
			}
		}
		return Result.PermissionNotAllowed;
	}

	@PostMapping("/reset")
	public Result reset(@RequestParam Long uid) throws Exception {
		if (super.canAccess()) {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update("123456".getBytes());
			String hashPwd = HexUtils.toHexString(messageDigest.digest());
			Users user = new Users();
			user.setId(uid);
			user.setPwd(hashPwd);
			return new Result(usersService.updateById(user));
		}
		return Result.PermissionNotAllowed;
	}

	@PostMapping("/revisepwd")
	public Result revisePwd(@RequestParam String pwd) throws Exception {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
		HttpSession session = request.getSession();

		Long uid = (Long)session.getAttribute("id");
		if (null == uid) {
			throw new UnLoginException();
		}

		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(pwd.getBytes());
		String hashPwd = HexUtils.toHexString(messageDigest.digest());
		Users user = new Users();
		user.setId(uid);
		user.setPwd(hashPwd);
		return new Result(usersService.updateById(user));
	}

	@PostMapping("assign")
	public Result asign(@RequestParam("uid") Long uid, @RequestParam("rids") List<Byte> rids) throws Exception {
		if (super.canAccess()) {
			return new Result(userRoleAssocService.saveBatch(uid, rids));
		}
		return Result.PermissionNotAllowed;
	}
}
