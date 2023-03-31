/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-10-20
 */
@Getter
@Setter
@TableName("table_fields_info")
public class TableFieldsInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 表名或视图名
	 */
	private String table_name;

	/**
	 * 字段名
	 */
	private String field_name;

	/**
	 * 展示名
	 */
	private String display_name;

	/**
	 * 展示名
	 */
	private Byte display_order;

	/**
	 * 是否展示
	 */
	private Boolean display;

	/**
	 * 是否可检索
	 */
	private Boolean searchable;

	/**
	 * 是否可编辑
	 */
	private Boolean editable;
}
