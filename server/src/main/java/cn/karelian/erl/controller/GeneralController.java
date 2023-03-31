/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.karelian.erl.Result;
import cn.karelian.erl.annotation.Log;
import cn.karelian.erl.service.GeneralService;
import cn.karelian.erl.DataTransferObject.VerifyCodeParam;
import cn.karelian.erl.DataTransferObject.LoginParam;
import cn.karelian.erl.Exceptions.NullRequestException;

@RestController
@RequestMapping("")
public class GeneralController {
	@Autowired
	private GeneralService generalService;

	@Log(value = "登陆", checkLogin = false)
	@PostMapping("/login")
	public Result login(@RequestBody LoginParam params) {
		return generalService.login(params);
	}

	@Log(value = "登出", checkLogin = false)
	@PostMapping("/logout")
	public void logout() {
		generalService.logout();
	}

	@Log(value = "首页信息")
	@GetMapping("/index")
	public Result index() throws NullRequestException {
		return generalService.index();
	}

	@Log("上传")
	@PostMapping("/upload")
	public Result upload(@RequestPart MultipartFile[] files) {
		return generalService.upload(files);
	}

	@Log("验证码发送")
	@PostMapping({ "/{controller}/{method}/send", "/{controller}/{method}/verify/send" })
	public Result send(@RequestBody VerifyCodeParam params) throws NullRequestException {
		return generalService.send(params);
	}

	@Log(value = "验证", checkLogin = false)
	@PostMapping("/{controller}/{method}/verify")
	public Result verify(@RequestBody VerifyCodeParam params) {
		return generalService.verify(params);
	}
}