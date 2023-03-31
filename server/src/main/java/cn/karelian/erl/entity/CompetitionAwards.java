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
@TableName("competition_awards")
public class CompetitionAwards implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 奖项名称
	 */
	private String award_name;

	/**
	 * 获奖作品
	 */
	private String work_name;

	/**
	 * 获奖等级
	 */
	private String level;

	/**
	 * 组织单位名称
	 */
	private String org_name;

	/**
	 * 组织单位类型
	 */
	private String org_type;

	/**
	 * 获奖时间
	 */
	private LocalDate date;

	/**
	 * 获奖人姓名
	 */
	private String author;

	/**
	 * 附件
	 */
	private String enclosures;

	/**
	 * 获奖人角色
	 */
	private Byte role;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false, fill = FieldFill.INSERT)
	private Boolean deleted;
}
