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
import cn.karelian.erl.service.PapersService;

/**
 * <p>
 * 管理论文的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
@RestController
@RequestMapping("/Papers")
public class PapersController {
	@Autowired
	private PapersService papersService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return papersService.index(params);
	}

	@Authorize
	@GetMapping("/Postgraduate/index")
	public Result postindex(@ModelAttribute IndexParam params) throws Exception {
		return papersService.postindex(params);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@RequestParam List<Integer> ids) throws Exception {
		return papersService.delete(ids);
	}

}
