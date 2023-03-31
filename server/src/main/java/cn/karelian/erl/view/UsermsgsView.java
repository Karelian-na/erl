package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Karelian_na
 * @since 2023-02-28
 */
@Getter
@Setter
@TableName("usermsgs_view")
public class UsermsgsView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private Byte gender;

	/**
	 * 年龄
	 */
	private Byte age;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 联系方式
	 */
	private String phone;

	/**
	 * 政治面貌
	 */
	private Byte political_status;

	/**
	 * 民族
	 */
	private Byte clan;

	/**
	 * 个人简介
	 */
	private String profile;

	/**
	 * 角色
	 */
	private String roles;

	/**
	 * 注册时间
	 */
	private LocalDateTime add_time;

	/**
	 * 绑定邮箱
	 */
	private String bind_email;

	/**
	 * 绑定邮箱
	 */
	private String bind_phone;

}
