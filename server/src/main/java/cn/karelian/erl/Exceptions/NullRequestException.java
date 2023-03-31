package cn.karelian.erl.Exceptions;

import cn.karelian.erl.StatusCode;

/*
 * @Author: Karelian_na
 */

public class NullRequestException extends ErlException {

	public NullRequestException() {
		super("空请求的访问!");
		this.Code = StatusCode.ERROR_EMPTY_REQUEST;
	}
	
}
