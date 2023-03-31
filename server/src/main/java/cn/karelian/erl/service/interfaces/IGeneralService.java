package cn.karelian.erl.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.VerifyCodeParam;
import cn.karelian.erl.DataTransferObject.VerifyCodeSendParam;
import cn.karelian.erl.DataTransferObject.LoginParam;
import cn.karelian.erl.Exceptions.NullRequestException;

public interface IGeneralService {
	public Result login(LoginParam params);

	public void logout();

	public Result index() throws NullRequestException;

	public Result upload(MultipartFile[] files) throws NullRequestException;

	public Result send(VerifyCodeSendParam params) throws NullRequestException;

	public Result verify(VerifyCodeParam params) throws NullRequestException;
}
