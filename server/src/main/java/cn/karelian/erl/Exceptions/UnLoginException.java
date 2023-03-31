/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.Exceptions;

import cn.karelian.erl.StatusCode;

public class UnLoginException extends ErlException {
	public UnLoginException() {
		super("登陆状态失效或未登录!");
		this.Code = StatusCode.ERROR_UN_LOGIN;
	}
}
