package cn.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Result;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.service.DegreesService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
@RestController
@RequestMapping("/Degrees")
public class DegreesController {
	@Autowired
	private DegreesService degreesService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return degreesService.index(params);
	}
}
