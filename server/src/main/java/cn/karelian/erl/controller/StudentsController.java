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
import cn.karelian.erl.entity.Students;
import cn.karelian.erl.service.StudentsService;

/**
 * <p>
 * 管理学生信息的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
public class StudentsController {
	@Autowired
	private StudentsService studentsService;

	@Authorize
	@GetMapping("/Students/index")
	public Result index(@ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return studentsService.studentindex(params);
	}

	@Authorize
	@GetMapping("/Posts/index")
	public Result postindex(@ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return studentsService.postindex(params);
	}

	@Authorize
	@PostMapping("/add")
	public Result edit(@RequestBody List<Students> entities) throws Exception {
		return studentsService.add(entities);
	}

	@Authorize
	@PutMapping("/edit")
	public Result edit(@RequestBody Students entity) throws Exception {
		return studentsService.edit(entity);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@RequestParam List<Long> ids) {
		return studentsService.delete(ids);
	}
}
