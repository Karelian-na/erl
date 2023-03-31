package cn.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.AuditParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.service.LaboratoryReservationsService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-02-15
 */
@RestController
@RequestMapping("/Reservations/Laboratory")
public class LaboratoryReservationsController {
	@Autowired
	private LaboratoryReservationsService laboratoryReservationsService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return laboratoryReservationsService.index(params);
	}

	@Authorize
	@PutMapping("/audit")
	public Result audit(@RequestBody AuditParam params) throws NullRequestException {
		return laboratoryReservationsService.audit(params);
	}
}
