package cn.karelian.erl.service;

import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cn.karelian.erl.utils.VerifyCodeCache;

@Service
public class SMSService {
	public static final long ValidDuration = 5L;

	@Async
	public boolean sendVerifyCode(VerifyCodeCache cache) {
		SimpleMailMessage message = new SimpleMailMessage();

		Random random = new Random();
		String code = "";
		for (int idx = 0; idx < 6; idx++) {
			code += random.nextInt(10);
		}
		message.setFrom("security@karelian.cn");
		message.setTo(cache.serial);
		message.setSubject("数学与统计系电子资源库");
		message.setText("【数学与统计系电子资源库】\r\n你正在使用邮件验证, 验证码为: " + code + "\r\n有效期为5分钟, 若不是您本人所为, 请忽略此邮件!");

		try {
			// javaMailSender.send(message);
			cache.code = code;
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
