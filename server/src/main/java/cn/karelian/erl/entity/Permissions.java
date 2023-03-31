/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理权限目录的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
@TableName("permissions")
public class Permissions implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final byte TYPE_MENU = 1;
	public static final byte TYPE_ITEM = 2;
	public static final byte TYPE_PAGE = 3;
	public static final byte TYPE_OPER = 4;

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 类型
	 */
	private Byte type;

	/**
	 * 状态
	 */
	private Boolean status;

	/**
	 * 地址
	 */
	private String url;

	/**
	 * 备注
	 */
	private String descrip;

	/**
	 * 父权限ID
	 */
	@TableField(fill = FieldFill.UPDATE)
	private Integer pid;

	/**
	 * 操作类型
	 */
	private Byte oper_type;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String add_user;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime add_time;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime update_time;
}
