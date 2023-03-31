package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-11-15
 */
@Getter
@Setter
@TableName("book_reservations")
public class BookReservations implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 用户ID
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long uid;

	/**
	 * 教材ID
	 */
	private Integer tid;

	/**
	 * 预订时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime add_time;

	/**
	 * 预订数量
	 */
	private Integer amount;

	/**
	 * 身份
	 */
	private String role;

	/**
	 * 预定留言
	 */
	private String message;

	/**
	 * 预订状态
	 */
	private Byte audit_status;

	/**
	 * 审核意见
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
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false, fill = FieldFill.INSERT)
	private Boolean deleted;
}
