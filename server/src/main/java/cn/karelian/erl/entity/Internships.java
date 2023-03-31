/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
@TableName("internships")
public class Internships implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 实习单位名称
	 */
	private String unit_name;

	/**
	 * 实习单位简介
	 */
	private String unit_descrip;

	/**
	 * 实习开始时间
	 */
	private LocalDate start_date;

	/**
	 * 实习时长
	 */
	private Short days;

	/**
	 * 最大申请人数
	 */
	private Short max_app_num;

	/**
	 * 申请截止时间
	 */
	private LocalDate deadline;

	/**
	 * 已申请人数
	 */
	private Short app_num;

	/**
	 * 发布时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime add_time;

	/**
	 * 发布人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String add_user;

	/**
	 * 说明
	 */
	private String descrip;

	/**
	 * 状态
	 */
	private Byte status;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false, fill = FieldFill.INSERT)
	private Byte deleted;
}
