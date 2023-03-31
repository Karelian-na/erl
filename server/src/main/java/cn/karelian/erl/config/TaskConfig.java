package cn.karelian.erl.config;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import cn.karelian.erl.ErlApplication;

@Configuration
@EnableScheduling
public class TaskConfig {
	@Scheduled(cron="0 0 0 1/3 * ?")
	private void clearTask() {
		File[] files = new File(ErlApplication.TempPath).listFiles();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -3);

		Long deadline = calendar.getTime().getTime();
		for (File file : files) {
			if (file.lastModified() < deadline) {
				file.delete();
			}
		}
	}

}
