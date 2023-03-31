package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@TableName("fields_info_view")
public class FieldsInfoView implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@TableField(select = false)
	private String table_name;

	private String field_name;

	/**
	 * 展示名
	 */
	private String display_name;

	/**
	 * 次序
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
