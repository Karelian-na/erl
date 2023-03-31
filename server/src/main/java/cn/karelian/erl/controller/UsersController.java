package cn.karelian.erl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.AsignRoleParam;
import cn.karelian.erl.DataTransferObject.AuthorizeParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.RevisePasswordParam;
import cn.karelian.erl.DataTransferObject.VerifyCodeParam;
import cn.karelian.erl.Exceptions.*;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.annotation.Log;
import cn.karelian.erl.entity.Usermsgs;
import cn.karelian.erl.service.UsersService;

/**
 * <p>
 * 管理用户的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */

@RestController
@RequestMapping("/Users")
public class UsersController {
	@Autowired
	private UsersService usersService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return usersService.index(params);
	}

	@Log
	@GetMapping("/paperAuthor")
	public Result paperauthor(@RequestParam Long id) {
		return usersService.getPaperAuthor(id);
	}

	@Authorize
	@PutMapping("/edit")
	public Result edit(@RequestBody Usermsgs params) throws Exception {
		return usersService.edit(params);
	}

	@Log("我的信息")
	@GetMapping("/self/index")
	public Result selfindex() throws NullRequestException {
		return usersService.selfindex();
	}

	@Log("我的信息修改")
	@PutMapping("/self/edit")
	public Result selfedit(@RequestBody Usermsgs params) throws NullRequestException {
		return usersService.selfedit(params);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@RequestParam long id) {
		return new Result(usersService.removeById(id));
	}

	@Authorize
	@GetMapping("/authorize")
	public Result authorizeindex(@RequestParam Long id,
			@RequestParam(required = false, defaultValue = "false") Boolean all)
			throws NullRequestException {
		return usersService.authorizeindex(id, all);
	}

	@Authorize
	@PutMapping("/authorize")
	public Result authorize(@RequestBody AuthorizeParam params) throws TransactionFailedException {
		return usersService.authorize(params);
	}

	@Authorize
	@PutMapping("/reset")
	public Result reset(@RequestBody List<Long> uids) {
		return usersService.reset(uids);
	}

	@Log(value = "修改密码", checkLogin = false)
	@PutMapping("/revisepwd")
	public Result selfreset(@RequestBody RevisePasswordParam params) throws NullRequestException {
		return usersService.selfreset(params);
	}

	@Authorize
	@GetMapping("/assign")
	public Result asignindex(@RequestParam List<Long> ids) throws NullRequestException {
		return usersService.asignindex(ids);
	}

	@Authorize
	@PutMapping("/assign")
	public Result asign(@RequestBody AsignRoleParam params) throws NullRequestException, TransactionFailedException {
		return usersService.asign(params);
	}

	@Log("账户绑定/修改")
	@PostMapping("/bind")
	public Result bind(@RequestBody VerifyCodeParam params) throws NullRequestException {
		return usersService.bind(params);
	}

	@Log(value = "获取验证方式", checkLogin = false)
	@GetMapping("/verifies")
	public Result getverifies(@RequestParam(required = false) String account) throws NullRequestException {
		return usersService.getverifies(account);
	}
}