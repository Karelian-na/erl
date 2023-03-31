package cn.karelian.erl.utils;

public enum ProjectType {

	/**
	 * 科研项目
	 */
	Research,

	/**
	 * 教研项目
	 */
	Teaching;

	private static final int START = 1;

	private int value;

	ProjectType() {
		value = ordinal() + START;
	}

	public int getValue() {
		return value;
	}
}
