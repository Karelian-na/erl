package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.Year;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理论文的表
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
@Getter
@Setter
@TableName("academic_papers")
public class Papers implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 论文标识
	 */
	private String unique_id;

	/**
	 * 论文标题
	 */
	private String title;

	/**
	 * 作者
	 */
	@TableField(exist = false, select = true)
	private List<PaperAuthorAssoc> authors;

	/**
	 * 发表期刊
	 */
	private String pub_jour;

	/**
	 * 发表年份
	 */
	private Year pub_year;

	/**
	 * 发表卷数
	 */
	private Byte pub_vol;

	/**
	 * 发表期数
	 */
	private Byte pub_term;

	/**
	 * 期刊收录情况
	 */
	private String inc;

	/**
	 * 学科
	 */
	@TableField(value = "disc_id")
	private Short disp;

	/**
	 * 附件
	 */
	private String enclosures;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false, fill = FieldFill.INSERT)
	private Boolean deleted;
}
