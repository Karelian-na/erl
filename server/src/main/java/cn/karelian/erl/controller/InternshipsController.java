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
import cn.karelian.erl.DataTransferObject.InternshipManageParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.Result;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.annotation.Log;
import cn.karelian.erl.entity.Internships;
import cn.karelian.erl.service.InternshipsService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/Internships")
public class InternshipsController {
	@Autowired
	private InternshipsService internshipsService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return internshipsService.index(params);
	}

	@Authorize
	@GetMapping("/appindex")
	public Result appindex(@ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return internshipsService.appindex(params);
	}

	@Authorize
	@PostMapping("/add")
	public Result add(@RequestBody Internships params) {
		return internshipsService.add(params);
	}

	@Authorize
	@PutMapping("/edit")
	public Result edit(@RequestBody Internships params) {
		return internshipsService.edit(params);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@RequestParam List<Integer> ids) {
		return internshipsService.delete(ids);
	}

	@Authorize
	@PostMapping("/apply")
	public Result apply(@RequestParam Integer id) throws TransactionFailedException, NullRequestException {
		return internshipsService.apply(id);
	}

	@Log("撤消申请")
	@PostMapping("/withdraw")
	public Result withdraw(@RequestParam Integer id) throws TransactionFailedException, NullRequestException {
		return internshipsService.withdraw(id);
	}

	@Authorize
	@GetMapping("/manage")
	public Result manageindex(@RequestParam Integer iid, @ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return internshipsService.manageindex(iid, params);
	}

	@Authorize
	@PutMapping("/manage")
	public Result manage(@RequestParam Integer iid, @RequestBody InternshipManageParam params)
			throws TransactionFailedException {
		return internshipsService.manage(iid, params);
	}
}
