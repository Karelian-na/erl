package cn.karelian.erl.utils;

import java.time.LocalDateTime;

public class VerifyCodeCache {
	public String code;
	public String serial;
	public VerifyType type;
	public String pageTraceId;

	public String lastSendUrl;
	public LocalDateTime lastSendTime;
	public VerifyType lastSendType;

	public String lastVerifiedUrl;
	public LocalDateTime lastVerifiedTime;

	public VerifyCodeCache() {
		this.clear();
		this.lastVerifiedUrl = "";
		this.lastSendUrl = "";
	}

	public void clear() {
		this.code = "";
		this.serial = "";
		this.type = null;
		this.pageTraceId = "";
		this.lastSendTime = null;
	}
}