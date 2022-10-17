/*
 * @Author: Karelian_na
 */
package com.karelian.erl.Exceptions;

import com.karelian.erl.StatusCode;

public class PermissionNotFoundException extends ErlException {

	public PermissionNotFoundException() {
		super("未经授权的访问!");
		this.Code = StatusCode.ERROR_PERMISSION_NOT_ALLOWED;
	}
}
