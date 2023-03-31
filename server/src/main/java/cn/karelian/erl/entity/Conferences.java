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
 * 学生参见本领域国内外重要学术会议表(研究生)
 * </p>
 *
 * @author baomidou
 * @since 2023-01-15
 */
@Getter
@Setter
@TableName("academic_conferences")
public class Conferences implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 参与人姓名
	 */
	private String parti_name;

	/**
	 * 会议名称
	 */
	private String name;

	/**
	 * 报告题目
	 */
	private String title;

	/**
	 * 报告时间
	 */
	private LocalDate date;
	
	/**
	 * 附件
	 */
	private String enclosures;

	/**
	 * 报告地点
	 */
	private String location;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false, fill = FieldFill.INSERT)
	private Boolean deleted;
}
