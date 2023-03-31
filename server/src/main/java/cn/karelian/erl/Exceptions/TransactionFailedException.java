package cn.karelian.erl.Exceptions;

import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;

public class TransactionFailedException extends ErlException {

	private Result result;

	public TransactionFailedException() {
		super(StatusCode.ERROR_TRANSACTION, null);
	}

	public TransactionFailedException(Result result) {
		super(StatusCode.ERROR_TRANSACTION, null);
		this.result = result;
	}

	public Result getResult() {
		return result;
	}
}
