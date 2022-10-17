/*
 * @Author: Karelian_na
 */
/*
 * @Author: Karelian_na
 */
/*
 * @Author: Karelian_na
 */
package com.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.karelian.erl.Result;
import com.karelian.erl.entity.Permissions;
import com.karelian.erl.service.impl.PermissionsService;

/**
 * <p>
 * 管理权限目录的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/permissions")
public class PermissionsController extends BaseController {
	@Autowired
	private PermissionsService permissionsService;

	@GetMapping("/edit")
	public Result getPermission(@RequestParam long id) throws Exception {
		if (super.canAccess()) {
			Permissions permi = permissionsService.getById(id);
			return new Result(permi != null, permi);
		}
		return Result.PermissionNotAllowed;
	}
	@PutMapping("/edit")
	public Result updatePermission(@RequestBody Permissions permission) throws Exception {
		if (super.canAccess()) {
			Result result = new Result();
			Permissions oldPermi = permissionsService.getById(permission.getId());
			if (null == oldPermi) {
				result.setMsg("权限不存在!");
				return result;
			}
	
			Byte curType = permission.getType() != null ? permission.getType() : oldPermi.getType();
			Integer pid = permission.getPid() == null ? permissionsService.getPidById(oldPermi.getId())
					: permission.getPid();
			Byte pType = pid != null ? permissionsService.getTypeById(pid) : 0;
	
			result.setSuccess(permissionsService.checkTypeAssoc(pType, curType));
			if (!result.isSuccess()) {
				result.setMsg("权限关联错误!");
				return result;
			}
	
			result.setSuccess(permissionsService.updateById(permission));
			return result;
		}
		return Result.PermissionNotAllowed;
	}


	@DeleteMapping("/delete")
	public Result deletePermission(@RequestParam long id) throws Exception {
		if (super.canAccess()) {
			boolean success = permissionsService.removeById(id);
			return new Result(success);
		}
		return Result.PermissionNotAllowed;
	}

	@PostMapping("/add")
	public Result addPermission(@RequestBody Permissions permission) throws Exception {
		if (super.canAccess()) {
			Result result = new Result();
	
			Integer id = permission.getId();
			long count = permissionsService.count(permissionsService.lambdaQuery().getWrapper().eq(Permissions::getId, id));
			if (count == 1) {
				result.setMsg("权限已存在!");
				return result;
			}
	
			Byte type = permission.getType();
			Byte pType = permissionsService.getTypeById(permission.getPid());
			result.setSuccess(permissionsService.checkTypeAssoc(pType, type));
			if (!result.isSuccess()) {
				result.setMsg("权限关联错误!");
			}
	
			result.setSuccess(permissionsService.save(permission));
			return result;
		}
		return Result.PermissionNotAllowed;
	}
}
