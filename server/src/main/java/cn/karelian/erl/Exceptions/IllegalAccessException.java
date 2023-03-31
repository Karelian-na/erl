/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.Exceptions;

import cn.karelian.erl.StatusCode;

public class IllegalAccessException extends ErlException {
	
	public IllegalAccessException() {
		super("非法的访问!");
		this.Code = StatusCode.ERROR_ILLEGAL_ACCESS;
	}
}
