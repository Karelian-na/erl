/*
 * @Author: Karelian_na
 */
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
import cn.karelian.erl.DataTransferObject.BookReservationParam;
import cn.karelian.erl.Result;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.annotation.Authorize;
import cn.karelian.erl.entity.Textbooks;
import cn.karelian.erl.service.TextbooksService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/Books")
public class TextbooksController {
	@Autowired
	private TextbooksService textbooksService;

	@Authorize
	@GetMapping("/index")
	public Result index(@ModelAttribute IndexParam params) throws Exception {
		return textbooksService.index(params);
	}

	@Authorize
	@GetMapping("/resindex")
	public Result resindex(@ModelAttribute IndexParam params) throws Exception {
		return textbooksService.resindex(params);
	}


	@Authorize
	@PostMapping("/add")
	public Result add(@RequestBody Textbooks textbook) {
		return textbooksService.add(textbook);
	}

	@Authorize
	@PutMapping("/edit")
	public Result edit(@RequestBody Textbooks textbook) {
		return textbooksService.edit(textbook);
	}

	@Authorize
	@PutMapping("/reserve")
	public Result reserve(@RequestBody BookReservationParam params) throws NullRequestException {
		return textbooksService.reserve(params);
	}

	@Authorize
	@DeleteMapping("/delete")
	public Result delete(@RequestParam List<Integer> ids) {
		return textbooksService.delete(ids);
	}
}
