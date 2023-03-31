/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理用户的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
@TableName("users")
public class Users implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 密码
	 */
	private String pwd;

	/**
	 * 最后登录IP
	 */
	private String last_login_ip;

	/**
	 * 最后登录时间
	 */
	private LocalDateTime last_login_time;

	/**
	 * 注册时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime add_time;

	/**
	 * 绑定邮箱
	 */
	private String bind_email;

	/**
	 * 绑定手机
	 */
	private String bind_phone;

	/**
	 * 是否初始化
	 */
	private Boolean is_init;
}
