package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-02-15
 */
@Getter
@Setter
@TableName("laboratory_reservations")
public class LaboratoryReservations implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 实验室ID
	 */
	private Integer lab_id;

	/**
	 * 预约人
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long add_uid;

	/**
	 * 预约日期
	 */
	private LocalDate day;

	/**
	 * 开始时间
	 */
	private LocalTime start;

	/**
	 * 结束时间
	 */
	private LocalTime end;

	/**
	 * 留言
	 */
	private String message;

	/**
	 * 预约时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime add_time;

	/**
	 * 审核状态
	 */
	private Byte audit_status;

	/**
	 * 审核时间
	 */
	private LocalDateTime audit_time;

	/**
	 * 审核人
	 */
	private String audit_user;

	/**
	 * 意见
	 */
	private String comment;
}
