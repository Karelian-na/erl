/*
 * @Author: Karelian_na
 */
package cn.karelian.erl;

import org.springframework.util.ObjectUtils;

import cn.karelian.erl.errors.FieldError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

	/**
	 * 指示当前请求是否成功!
	 */
	private int code;

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
	public static final Result InvalidArgument = new Result("无效的参数!");
	public static final Result UnLogin = new Result("登陆状态失效或未登录!");

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

	public Result(int code, String msg) {
		this.success = false;
		this.code = code;
		this.data = null;
		this.msg = msg;
	}

	public Result(boolean success, Object data) {
		this.success = success;
		this.data = success ? data : null;
		this.msg = null;
	}

	public Result(boolean success, String msg, Object data) {
		this.success = success;
		this.msg = msg;
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(Class<T> clszz) {
		return (T) this.data;
	}

	public static Result internalError(String msg) {
		if (ObjectUtils.isEmpty(msg)) {
			msg = "服务器内部错误!";
		}
		return new Result(msg);
	}

	public static Result paramError(String paramName) {
		return new Result("无效的参数" + paramName + "!");
	}

	public static Result paramError(String paramName, String msg) {
		return new Result("无效的参数" + paramName + "!" + msg);
	}

	public static Result strTooLongError(String paramName) {
		return new Result("参数" + paramName + "过长!");
	}

	public static Result emptyError(String paramName) {
		return new Result(FieldError.EMPTY, "参数" + paramName + "不能为空!");
	}

	public static Result fieldError(String field, Integer errCode) {
		String msg = "参数";
		switch (errCode) {
			case FieldError.EMPTY:
				msg += field + "不能为空!";
				break;
			case FieldError.TOO_LARGE:
			case FieldError.TOO_SMALL:
				msg += field + "不合范围!";
				break;
			case FieldError.TOO_SHORT:
			case FieldError.TOO_LONG:
			case FieldError.FORMAT:
				msg += field + "不符格式!";
				break;
			default:
				msg = "未知错误!";
				break;
		}
		return new Result(errCode, msg);
	}

	public static Result fieldError(String field, Integer errCode, String msg) {
		return new Result(errCode, "参数" + field + msg);
	}

	public static Result emptyParamError() {
		return new Result("请求参数不能为空!");
	}

}
