package cn.karelian.erl.utils;

public enum ApplicationStatus {
	/**
	 * 已申请(用户已申请)
	 */
	Applied,

	/**
	 * 已撤销(用户本人撤销)
	 */
	Revoked,

	/**
	 * 已被撤销(管理者撤销)
	 */
	beenRevoked,

	/**
	 * 进行中(实习进行中)
	 */
	InProgress,

	/**
	 * 已完成(实习已完成)
	 */
	Finished,

	/**
	 * 已取消(实习已取消)
	 */
	Canceled;

	private static final int START = 1;

	private int value;

	ApplicationStatus() {
		value = ordinal() + START;
	}

	public int getValue() {
		return value;
	}
}
