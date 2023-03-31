package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@TableName("declarations_view")
public class DeclarationsView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	private String add_user;

	/**
	 * 申报单号
	 */
	private Integer id;

	/**
	 * 申报人ID
	 */
	private Long add_uid;

	/**
	 * 申报所在表id
	 */
	private Integer ref_id;

	/**
	 * 说明
	 */
	private String message;

	/**
	 * 状态
	 */
	private Byte audit_status;

	/**
	 * 申报时间
	 */
	private LocalDateTime add_time;

	/**
	 * 意见
	 */
	private String comment;

	/**
	 * 审核时间
	 */
	private LocalDateTime audit_time;

	/**
	 * 审核人
	 */
	private String audit_user;

	/**
	 * 分组
	 */
	@TableField("`group`")
	private String group;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false)
	private Boolean deleted;
}
