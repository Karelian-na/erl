/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Result;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.entity.Teachers;
import cn.karelian.erl.service.TeachersService;

/**
 * <p>
 * 管理教师信息的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
public class TeachersController {
	@Autowired
	private TeachersService teachersService;

	@Authorize
	@GetMapping("/Teachers/index")
	public Result index(@ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return teachersService.teacherindex(params);
	}

	@Authorize
	@GetMapping("/Tutors/index")
	public Result tutindex(@ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return teachersService.tutorindex(params);
	}

	@Authorize
	@PostMapping("/Teachers/add")
	public Result edit(@RequestBody List<Teachers> entities) throws Exception {
		return teachersService.add(entities);
	}

	@Authorize
	@PutMapping("/Teachers/edit")
	public Result edit(@RequestBody Teachers entity) throws Exception {
		return teachersService.edit(entity);
	}

	@Authorize
	@DeleteMapping("/Teachers/delete")
	public Result delete(@RequestParam List<Long> ids) {
		return teachersService.delete(ids);
	}
}
