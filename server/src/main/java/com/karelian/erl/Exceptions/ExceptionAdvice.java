/*
 * @Author: Karelian_na
 */
/*
 * @Author: Karelian_na
 */
package com.karelian.erl.Exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.karelian.erl.Result;

@RestControllerAdvice
public class ExceptionAdvice {
	@ExceptionHandler(Exception.class)
	public Result doException(Exception ex) {
		return new Result(false, ex.getMessage(), null);
	}
}
