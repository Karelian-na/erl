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
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
@Getter
@Setter
@TableName("patents")
public class Patents implements Serializable {

	private static final long serialVersionUID = 1L;

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
	 * 作者
	 */
	private String author;

	/**
	 * 专利号
	 */
	private String number;

	/**
	 * 申请时间
	 */
	private LocalDate pub_date;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 附件
	 */
	private String enclosures;

	/**
	 * 授权时间
	 */
	private LocalDate auth_date;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false, fill = FieldFill.INSERT)
	private Boolean deleted;
}
