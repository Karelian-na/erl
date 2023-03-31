package cn.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.entity.Permissions;
import cn.karelian.erl.service.PermissionsService;

/**
 * <p>
 * 管理权限目录的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/Permissions")
public class PermissionsController {
	@Autowired
	private PermissionsService permissionsService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return permissionsService.index(params);
	}

	@Authorize
	@PutMapping("/edit")
	public Result updatePermission(@RequestBody Permissions permission) throws Exception {
		return permissionsService.update(permission);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result deletePermission(@RequestParam long ids) throws Exception {
		return new Result(permissionsService.removeById(ids));
	}

	@Authorize
	@PostMapping("/add")
	public Result addPermission(@RequestBody Permissions permission) throws Exception {
		return permissionsService.add(permission);
	}
}
