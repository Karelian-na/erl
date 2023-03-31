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
 * @since 2023-02-04
 */
@Getter
@Setter
@TableName("teaching_awards")
public class TeachingAwards implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 成果名称
	 */
	private String name;

	/**
	 * 奖项类型
	 */
	private Byte type;

	/**
	 * 奖项等级
	 */
	private Byte level;

	/**
	 * 成果完成人
	 */
	private String author;

	/**
	 * 单位署名次序
	 */
	private Byte unit_sig_order;

	/**
	 * 完成人署名次序
	 */
	private Byte comp_sig_order;

	/**
	 * 附件
	 */
	private String enclosures;

	/**
	 * 获奖时间
	 */
	private LocalDate date;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false, fill = FieldFill.INSERT)
	private Boolean deleted;
}
