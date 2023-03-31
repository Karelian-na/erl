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
@TableName("textbooks")
public class Textbooks implements Serializable {

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
	 * 封面
	 */
	private String cover;

	/**
	 * 类别
	 */
	private String type;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 出版社
	 */
	private String publisher;

	/**
	 * ISBN
	 */
	private String isbn;

	/**
	 * 价格
	 */
	private Float price;

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
	 * 添加时间
	 */
	@JsonIgnore
	@TableLogic
	@TableField(select = false, fill = FieldFill.INSERT)
	private Boolean deleted;
}
