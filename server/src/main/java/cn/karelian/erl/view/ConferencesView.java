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
@TableName("conferences_view")
public class ConferencesView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
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
	@TableField(select = false)
	private Byte deleted;
}
