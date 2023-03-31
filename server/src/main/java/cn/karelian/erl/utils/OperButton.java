package cn.karelian.erl.utils;

import cn.karelian.erl.entity.Permissions;

public class OperButton {
	public String icon;
	public String type;
	public String title;
	public Byte oper_type;
	public static final OperButton DEL_OPER_BUTTON = new OperButton("delete", "删除", 3);

	public OperButton(Permissions Permi) {
		String url = Permi.getUrl();
		this.type = url.substring(url.lastIndexOf("/") + 1);
		this.icon = Permi.getIcon();
		this.title = Permi.getName();
		this.oper_type = Permi.getOper_type();
	}

	private OperButton(String type, String title, int oper_type) {
		this.type = this.icon = type;
		this.title = title;
		this.oper_type = (byte) oper_type;
	}

	public static OperButton ofDelete(String title, int oper_type) {
		return new OperButton("delete", title, oper_type);
	}
}
