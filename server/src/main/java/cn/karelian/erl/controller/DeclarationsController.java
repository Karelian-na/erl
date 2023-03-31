package cn.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.AuditParam;
import cn.karelian.erl.DataTransferObject.DeclareParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.service.DeclarationsService;

/**
 * <p>
 * 管理各种申报的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-02-04
 */
@RestController
@RequestMapping("/Declarations")
public class DeclarationsController {
	@Autowired
	private DeclarationsService declarationsService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return declarationsService.index(params);
	}

	@Authorize
	@GetMapping("/{type}/declare")
	public Result delcareindex(@PathVariable String type) throws IllegalAccessException {
		return declarationsService.declareindex(type);
	}

	@Authorize
	@PostMapping("/{type}/declare")
	public Result declare(@PathVariable String type, @RequestBody DeclareParam params) throws TransactionFailedException {
		return declarationsService.declare(type, params);
	}

	@Authorize
	@GetMapping("/audit")
	public Result auditindex(@RequestParam String group, @RequestParam Integer id) {
		return declarationsService.auditindex(group, id);
	}

	@Authorize
	@PutMapping("/audit")
	public Result audit(@RequestBody AuditParam params) throws NullRequestException {
		return declarationsService.audit(params);
	}
}
