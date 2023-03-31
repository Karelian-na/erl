package cn.karelian.erl.controller;

import java.util.List;

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
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.entity.Courses;
import cn.karelian.erl.service.CoursesService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/Courses")
public class CoursesController {
	@Autowired
	private CoursesService coursesService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return coursesService.index(params);
	}

	@Authorize
	@GetMapping("/Experiment/index")
	public Result expindex(@ModelAttribute IndexParam params) throws Exception {
		return coursesService.expindex(params);
	}

	@Authorize
	@PostMapping("/add")
	public Result add(@RequestBody Courses course) {
		return coursesService.add(course);
	}

	@Authorize
	@PostMapping("/Experiment/add")
	public Result expadd(@RequestBody Courses course) {
		return coursesService.expadd(course);
	}

	@Authorize
	@PutMapping("/edit")
	public Result edit(@RequestBody Courses course) {
		return coursesService.edit(course);
	}

	@Authorize
	@PutMapping("/Experiment/edit")
	public Result expedit(@RequestBody Courses course) {
		return coursesService.expedit(course);
	}

	@Authorize
	@DeleteMapping({ "/delete", "/Experiment/delete" })
	public Result delete(@RequestParam List<Integer> ids) {
		return coursesService.delete(ids);
	}
}
