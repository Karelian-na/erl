package com.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.karelian.erl.service.impl.LogsService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/logs")
@ResponseBody
public class LogsController {

	@Autowired
	private LogsService logsService;

	@GetMapping("/index")
	public String index() {
		return "WAf";

	}

	@DeleteMapping
	public boolean delete(@RequestParam int id) {
		return logsService.removeById(id);
	}
}
