package cn.karelian.erl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.AuditParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.annotation.Log;
import cn.karelian.erl.service.BookReservationsService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/Reservations/Book")
public class BookReservationsController {
	@Autowired
	private BookReservationsService bookReservationsService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return bookReservationsService.index(params);
	}

	@Log("我的预定")
	@GetMapping("/self/index")
	public Result selfindex(@ModelAttribute IndexParam params)
			throws NullRequestException, IllegalAccessException, PermissionNotFoundException {
		return bookReservationsService.selfindex(params);
	}

	@Authorize
	@PutMapping("/audit")
	public Result audit(@RequestBody AuditParam params) throws NullRequestException {
		return bookReservationsService.audit(params);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@RequestParam("ids") List<Integer> ids) {
		return new Result(bookReservationsService.removeBatchByIds(ids));
	}

	@Log("我的预定删除")
	@DeleteMapping("/self/delete")
	public Result selfDelete(@RequestParam List<Integer> ids) throws NullRequestException {
		return bookReservationsService.selfdelete(ids);
	}
}
