/*
 * @Author: Karelian_na
 */
package com.karelian.erl.Exceptions;

public class ErlException extends Exception {
	protected int Code;

	public int getCode() {
		return this.Code;
	}

	public ErlException(String msg) {
		super(msg);
	}

	public ErlException(int code, String msg) {
		super(msg);
		this.Code = code;
	}
}
