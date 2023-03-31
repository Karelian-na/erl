package cn.karelian.erl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Result;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.service.ProjectsService;
import cn.karelian.erl.utils.ProjectType;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-01-15
 */
@RestController
@RequestMapping("/Projects")
public class ProjectsController {
	@Autowired
	private ProjectsService projectsService;

	@Authorize
	@GetMapping("/Teaching/index")
	public Result teachingindex(@ModelAttribute IndexParam params) throws Exception {
		return projectsService.index(ProjectType.Teaching, params);
	}

	@Authorize
	@GetMapping("/Research/index")
	public Result researchindex(@ModelAttribute IndexParam params) throws Exception {
		return projectsService.index(ProjectType.Research, params);
	}

	@Authorize
	@DeleteMapping("/Teaching/delete")
	public Result teachingdelete(@RequestParam List<Integer> ids) {
		return projectsService.delete(ProjectType.Teaching, ids);
	}

	@Authorize
	@DeleteMapping("/Research/delete")
	public Result researchdelete(@RequestParam List<Integer> ids) {
		return projectsService.delete(ProjectType.Research, ids);
	}
}
