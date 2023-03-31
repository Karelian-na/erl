package cn.karelian.erl.config;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.utils.LoginInfomationUtil;

@Configuration
public class MyBatisConfig {

	@Bean
	MybatisPlusInterceptor getInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		return interceptor;
	}

	@Bean
	MetaObjectHandler metaObjectHandler() {
		return new MetaObjectHandler() {
			@Override
			public void insertFill(MetaObject metaObject) {
				this.strictInsertFill(metaObject, "add_time", LocalDateTime.class, LocalDateTime.now());
				this.strictInsertFill(metaObject, "deleted", Boolean.class, false);
				try {
					HttpSession session = LoginInfomationUtil.getSession();
					this.strictInsertFill(metaObject, "add_uid", Long.class, (Long) session.getAttribute("id"));
					this.strictInsertFill(metaObject, "add_user", String.class, (String) session.getAttribute("name"));
				} catch (NullRequestException e) {
				}
			}

			@Override
			public void updateFill(MetaObject metaObject) {
				this.strictUpdateFill(metaObject, "update_time", LocalDateTime.class, LocalDateTime.now());
				this.strictUpdateFill(metaObject, "update_user", String.class, LoginInfomationUtil.getUserName());
			}

		};
	}
}
