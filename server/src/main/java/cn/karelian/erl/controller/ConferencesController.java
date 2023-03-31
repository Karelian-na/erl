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
import cn.karelian.erl.service.ConferencesService;

/**
 * <p>
 * 学生参见本领域国内外重要学术会议表(研究生) 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-01-15
 */
@RestController
@RequestMapping("/Conferences")
public class ConferencesController {
	@Autowired
	private ConferencesService conferencesServices;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return conferencesServices.index(params);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result teachingdelete(@RequestParam List<Integer> ids) {
		return conferencesServices.delete(ids);
	}
}
