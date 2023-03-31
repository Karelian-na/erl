/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.Exceptions;

import cn.karelian.erl.StatusCode;

public class PermissionNotFoundException extends ErlException {

	public PermissionNotFoundException() {
		super("未知权限的访问!");
		this.Code = StatusCode.ERROR_PERMISSION_NOT_ALLOWED;
	}
}
