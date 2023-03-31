package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-02-14
 */
@Getter
@Setter
public class Courses implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 课程代码
	 */
	private String number;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 类型
	 */
	private Byte category;

	/**
	 * 开课单位
	 */
	private String unit;

	/**
	 * 学分
	 */
	private Float credit;

	/**
	 * 理论学时
	 */
	private Short theo_hours;

	/**
	 * 实验学时
	 */
	private Short exp_hours;

	/**
	 * 上机学时
	 */
	private Short comp_hours;

	/**
	 * 实践学时
	 */
	private Short prac_hours;

	/**
	 * 考核方式
	 */
	private Byte ass_method;

	/**
	 * 修读性质
	 */
	private Byte nature;

	/**
	 * 添加人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String add_user;

	/**
	 * 添加时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime add_time;

	/**
	 * 是否删除
	 */
	@JsonIgnore
	@TableLogic
	@TableField(select = false, fill = FieldFill.INSERT)
	private Byte deleted;
}
