/*
 * @Author: Karelian_na
 */
package com.karelian.erl.Exceptions;

import com.karelian.erl.StatusCode;

public class UnLoginException extends ErlException {
	public UnLoginException() {
		super("登陆状态失效或未登录!");
		this.Code = StatusCode.ERROR_UN_LOGIN;
	}
}
