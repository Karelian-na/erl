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
@TableName("patents_view")
public class PatentsView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
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
	private Byte type;

	/**
	 * 状态
	 */
	private Byte status;

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
	@TableField(select = false)
	private Byte deleted;
}
