package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Karelian_na
 * @since 2023-03-19
 */
@Getter
@Setter
@TableName("projects_view")
public class ProjectsView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 项目名称
	 */
	private String name;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 作者排序
	 */
	private Byte auth_order;

	/**
	 * 项目来源
	 */
	private String source;

	/**
	 * 起始时间
	 */
	private LocalDate start_date;

	/**
	 * 截止时间
	 */
	private LocalDate end_date;

	/**
	 * 项目总金额
	 */
	private Integer tot_funding;

	/**
	 * 到账金额
	 */
	private Integer rec_funding;

	/**
	 * 项目类型
	 */
	private String type;

	/**
	 * 附件
	 */
	private String enclosures;

	/**
	 * 分组
	 */
	private Byte category;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false)
	private Byte deleted;
}
