package cn.karelian.erl.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.service.MatersService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-11-17
 */
@RestController
@RequestMapping("/Maters/{group}/{category}")
public class MatersController {
	@Autowired
	private MatersService matersService;

	@Authorize
	@GetMapping("/index")
	public Result index(@PathVariable String group, @PathVariable String category, @ModelAttribute IndexParam params)
			throws Exception {
		return matersService.index(group, category, params);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@PathVariable String group, @PathVariable String category, @RequestParam List<Integer> ids) {
		return matersService.delete(ids);
	}

	@Authorize
	@GetMapping("/download")
	public Result download(@PathVariable String group, @PathVariable String category,
			@RequestParam List<Integer> ids) {
		return matersService.download(group, category, ids);
	}

	@Authorize
	@PostMapping("/upload")
	public Result upload(@PathVariable String group, @PathVariable String category, @RequestPart MultipartFile file) {
		return matersService.upload(group, category, file);
	}
}
