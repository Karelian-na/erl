package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理各种申报的表
 * </p>
 *
 * @author baomidou
 * @since 2023-02-04
 */
@Getter
@Setter
public class Declarations implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 申报单号
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 分组
	 */
	@TableField(value = "`group`")
	private String group;

	/**
	 * 添加人
	 */
	@TableField(fill = FieldFill.INSERT)
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
	@TableField(fill = FieldFill.INSERT)
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
}
