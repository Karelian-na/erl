package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("teacmsgs_view")
public class TeacmsgsView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 职工号
	 */
	private Long id;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 年龄
	 */
	private Byte age;

	/**
	 * 性别
	 */
	private Byte gender;

	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 联系方式
	 */
	private String phone;

	/**
	 * 职位
	 */
	private Byte level;

	/**
	 * 学历
	 */
	private Byte degree;

	/**
	 * 学位
	 */
	private Byte bachelor;

	/**
	 * 是否导师
	 */
	private Boolean is_tutor;
}
