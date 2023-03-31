package cn.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.AuthorizeParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.entity.Roles;
import cn.karelian.erl.service.RolesService;

/**
 * <p>
 * 管理角色的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/Roles")
public class RolesController {
	@Autowired
	private RolesService rolesService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return rolesService.index(params);
	}

	@Authorize
	@GetMapping("/authorize")
	public Result getAuthorized(@RequestParam Integer id,
			@RequestParam(required = false, defaultValue = "false") Boolean all) {
		return rolesService.authorizeindex(id, all);
	}

	@Authorize
	@PutMapping("/authorize")
	public Result authorize(@RequestBody AuthorizeParam params) throws TransactionFailedException {
		return rolesService.authorize(params);
	}

	@Authorize
	@PostMapping("/add")
	public Result add(@RequestBody Roles role) {
		return rolesService.add(role);
	}

	@Authorize
	@PutMapping("/edit")
	public Result edit(@RequestBody Roles role) {
		return rolesService.edit(role);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@RequestParam Integer id) {
		return rolesService.delete(id);
	}
}
