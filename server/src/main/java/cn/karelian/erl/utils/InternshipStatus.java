package cn.karelian.erl.utils;

public enum InternshipStatus {
	/**
	 * 招募中
	 */
	Recruiting,

	/**
	 * 进行中
	 */
	InProgress,

	/**
	 * 已取消
	 */
	Canceled,

	/**
	 * 已完成
	 */
	Finished;

	private static final int START = 1;

	private int value;

	InternshipStatus() {
		value = ordinal() + START;
	}

	public int getValue() {
		return value;
	}

	public static byte getMax() {
		return (byte) (values().length + 1);
	}
}
