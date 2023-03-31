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

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.LaboratoryReservationParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Result;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.entity.Laboratories;
import cn.karelian.erl.service.LaboratoriesService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/Laboratories")
public class LaboratoriesController {
	@Autowired
	private LaboratoriesService laboratoriesService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return laboratoriesService.index(params);
	}

	@Authorize
	@GetMapping("/resindex")
	public Result resindex(@ModelAttribute IndexParam params) throws Exception {
		return laboratoriesService.resindex(params);
	}

	@Authorize
	@PostMapping("/add")
	public Result add(@RequestBody Laboratories laboratory) throws NullRequestException {
		return laboratoriesService.add(laboratory);
	}

	@Authorize
	@PutMapping("/edit")
	public Result edit(@RequestBody Laboratories laboratory) {
		return laboratoriesService.edit(laboratory);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@RequestParam List<Integer> ids) {
		return laboratoriesService.delete(ids);
	}

	@Authorize
	@PutMapping("/reserve")
	public Result reserve(@RequestBody LaboratoryReservationParam params) {
		return laboratoriesService.reserve(params);
	}
}
