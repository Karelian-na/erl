/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.Exceptions;

import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.core.NestedRuntimeException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;

@RestControllerAdvice
public class ExceptionAdvice {
	@Order(0)
	@ExceptionHandler(Exception.class)
	public Result doException(Exception ex) {
		Result result = new Result(false, ex.getMessage(), null);
		if (ex instanceof ErlException) {
			result.setCode(((ErlException) ex).getCode());
		} else if (ex instanceof UndeclaredThrowableException) {
			ErlException throwable = (ErlException) ((UndeclaredThrowableException) ex).getUndeclaredThrowable();
			result.setMsg(throwable.getMessage());
			result.setCode(throwable.getCode());
		} else if (ex instanceof NestedRuntimeException) {
			result.setCode(StatusCode.ERROR_INTERNAL);
			result.setMsg("服务器内部错误!");
		}
		return result;
	}
}
