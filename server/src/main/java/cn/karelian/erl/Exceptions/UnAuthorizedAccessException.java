/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.Exceptions;

import cn.karelian.erl.StatusCode;

public class UnAuthorizedAccessException extends ErlException {
	public UnAuthorizedAccessException() {
		super("未经授权的访问!");
		this.Code = StatusCode.ERROR_UN_AUTHORIZED;
	}
}
