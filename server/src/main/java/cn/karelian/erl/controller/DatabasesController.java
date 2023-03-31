package cn.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.ViewEditParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.Result;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.service.DatabasesService;

@RestController
@RequestMapping("/Databases")
public class DatabasesController {
	@Autowired
	private DatabasesService databasesService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return databasesService.index(params);
	}

	@Authorize
	@GetMapping("/edit")
	public Result editindex(@RequestParam String viewName) {
		return databasesService.editindex(viewName);
	}

	@Authorize
	@PutMapping("/edit")
	public Result edit(@RequestBody ViewEditParam params) throws TransactionFailedException {
		return databasesService.edit(params);
	}
}
