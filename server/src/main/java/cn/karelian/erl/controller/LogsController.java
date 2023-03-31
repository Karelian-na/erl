/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Result;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.entity.Logs;
import cn.karelian.erl.service.LogsService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/Logs")
public class LogsController {
	@Autowired
	private LogsService logsService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {

		QueryWrapper<Logs> qw = new QueryWrapper<>();
		qw.orderBy(true, false, "date");

		return logsService.index(params, qw);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@RequestParam("ids") List<Integer> ids) {
		return new Result(logsService.removeBatchByIds(ids));
	}
}
