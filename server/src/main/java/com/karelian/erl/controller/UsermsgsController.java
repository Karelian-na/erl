/*
 * @Author: Karelian_na
 */
package com.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.karelian.erl.Result;
import com.karelian.erl.Exceptions.NullRequestException;
import com.karelian.erl.Exceptions.PermissionNotFoundException;
import com.karelian.erl.Exceptions.UnLoginException;
import com.karelian.erl.entity.Usermsgs;
import com.karelian.erl.service.impl.UsermsgsService;

/**
 * <p>
 * 管理用户基本信息的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/users")
public class UsermsgsController extends BaseController {
	@Autowired
	private UsermsgsService usermsgsService;

	@GetMapping("/edit")
	public Result getUsermsg(@RequestParam long id)
			throws NullRequestException, PermissionNotFoundException, UnLoginException {
		if (super.canAccess()) {
			Usermsgs user = usermsgsService.getById(id);
			return new Result(user != null, user);
		}
		return new Result(false, "未经授权的操作!");
	}

	@PutMapping("/update")
	public Result updateUser(@RequestParam Usermsgs user)
			throws NullRequestException, PermissionNotFoundException, UnLoginException {
		if (super.canAccess()) {
			boolean success = usermsgsService.updateById(user);
			return new Result(success);
		}
		return new Result(false, "未经授权的操作!");
	}

}
