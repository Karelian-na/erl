package cn.karelian.erl.utils;

public enum CompetitionRole {
	/**
	 * 已申请(用户已申请)
	 */
	PostGraduate,

	/**
	 * 已撤销(用户本人撤销)
	 */
	Teacher;

	private static final int START = 1;

	private int value;

	CompetitionRole() {
		value = ordinal() + START;
	}

	public int getValue() {
		return value;
	}
}
