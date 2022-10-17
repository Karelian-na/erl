/*
 * @Author: Karelian_na
 */
package com.karelian.erl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

	/**
	 * 指示当前请求是否成功!
	 */
	private boolean success;

	/**
	 * 指示当前请求附带消息!
	 */
	private String msg;

	/**
	 * 指示当前请求返回的数据!
	 */
	private Object data;
	

	public static final Result PermissionNotAllowed = new Result("未经授权的操作!");

	public Result() {
		this.success = false;
		this.data = null;
		this.msg = "";
	}

	public Result(boolean success) {
		this.success = success;
		this.data = null;
		this.msg = null;
	}

	public Result(String msg) {
		this.success = false;
		this.data = null;
		this.msg = msg;
	}

	public Result(boolean success, Object data) {
		this.success = success;
		this.data = data;
		this.msg = null;
	}

	public Result(boolean success, String msg, Object data) {
		this.success = success;
		this.msg = msg;
		this.data = data;
	}
}
