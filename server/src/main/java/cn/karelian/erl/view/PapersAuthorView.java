package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.Year;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Karelian_na
 * @since 2023-03-22
 */
@Getter
@Setter
@TableName("papers_author_view")
public class PapersAuthorView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 论文标题
	 */
	private String title;

	/**
	 * 用户ID
	 */
	private Long uid;

	private String author;

	/**
	 * 获奖人角色
	 */
	private Byte role;

	/**
	 * 署名次序
	 */
	@TableField("`order`")
	private Byte order;

	/**
	 * 是否通讯作者
	 */
	private Boolean is_corresponding;

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
	private Byte inc;

	/**
	 * 名称
	 */
	private String disp;

	/**
	 * 附件
	 */
	private String enclosures;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false)
	private Boolean deleted;

	/**
	 * 状态
	 */
	@JsonIgnore
	@TableField(select = false)
	private Byte audit_status;
}
