package cn.karelian.erl.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理教师信息的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
@TableName("teachers")
public class Teachers implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 职工号
	 */
	private Long id;

	/**
	 * 职位
	 */
	private String level;

	/**
	 * 学历
	 */
	private String degree;

	/**
	 * 学位
	 */
	private String bachelor;

	/**
	 * 是否是导师
	 */
	private Boolean is_tutor;
}
