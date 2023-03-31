package cn.karelian.erl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.service.CompetitionAwardsService;
import cn.karelian.erl.service.TeachingAwardsService;
import cn.karelian.erl.utils.CompetitionRole;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
@RestController
@RequestMapping("/Awards")
public class AwardsController {
	@Autowired
	private TeachingAwardsService teachingAwardsService;
	@Autowired
	private CompetitionAwardsService competitionAwardsService;

	@Authorize
	@GetMapping("/Teaching/index")
	public Result teachingindex(@ModelAttribute IndexParam params) throws Exception {
		return teachingAwardsService.index(params);
	}

	@Authorize
	@GetMapping("/Competition/index")
	public Result compindex(@ModelAttribute IndexParam params) throws Exception {
		return competitionAwardsService.index(CompetitionRole.PostGraduate, params);
	}

	@Authorize
	@GetMapping("/Teacher/index")
	public Result teacindex(@ModelAttribute IndexParam params) throws Exception {
		return competitionAwardsService.index(CompetitionRole.Teacher, params);
	}

	@Authorize
	@DeleteMapping("/Teaching/delete")
	public Result teachingdelete(@RequestParam List<Integer> ids) throws Exception {
		return teachingAwardsService.delete(ids);
	}

	@Authorize
	@DeleteMapping({ "/Teacher/delete", "/Competition/delete" })
	public Result compdelete(@RequestParam List<Integer> ids) throws Exception {
		return competitionAwardsService.delete(ids);
	}

}
