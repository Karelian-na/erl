package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@TableName("stumsgs_view")
public class StumsgsView implements Serializable {

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
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 联系方式
	 */
	private String phone;

	/**
	 * 专业名称
	 */
	private String major;

	/**
	 * 班级
	 */
	@TableField("class")
	@JsonProperty("class")
	private String class_name;

	/**
	 * 学历
	 */
	private Byte degree;

	/**
	 * 所在年级
	 */
	private Short session;
}
