package cn.karelian.erl.utils;

public enum DeclarationType {
	/**
	 * 教学成果奖
	 */
	Teaching,

	/**
	 * 竞赛获奖(研究生)
	 */
	Competition,

	/**
	 * 教师重要奖项
	 */
	Teacher,

	/**
	 * 专利
	 */
	Patent,

	/**
	 * 论文
	 */
	Paper,

	/**
	 * 学术会议
	 */
	Conference,

	/**
	 * 教/科研项目
	 */
	Project;

	private static final int START = 1;

	private int value;

	DeclarationType() {
		value = ordinal() + START;
	}

	public int getValue() {
		return value;
	}
}
