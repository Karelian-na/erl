/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.Exceptions;

import cn.karelian.erl.StatusCode;

public class InvalidArgumentException extends ErlException {
	public InvalidArgumentException(String argName) {
		super("无效的参数:" + argName);
		this.Code = StatusCode.ERROR_INVALID_ARGUMENT;
	}


	public InvalidArgumentException(String argName, String msg) {
		super("无效的参数:" + argName + "!" + msg);
		this.Code = StatusCode.ERROR_INVALID_ARGUMENT;
	}
}
