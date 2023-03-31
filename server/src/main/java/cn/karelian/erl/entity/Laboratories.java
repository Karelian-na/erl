/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@TableName("laboratories")
public class Laboratories implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 编号
	 */
	private String number;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 容量
	 */
	private Byte capacity;

	/**
	 * 位置
	 */
	private String addr;

	/**
	 * 负责人
	 */
	private String commander;

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
