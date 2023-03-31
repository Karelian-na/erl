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
 * 
 * </p>
 *
 * @author Karelian_na
 * @since 2023-03-13
 */
@Getter
@Setter
public class Applications implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 实习任务ID
	 */
	private Integer iid;

	/**
	 * 实习人ID
	 */
	private Long add_uid;

	/**
	 * 状态
	 */
	private Byte status;

	/**
	 * 申请时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime add_time;

	/**
	 * 操作时间
	 */
	private LocalDateTime oper_time;

	/**
	 * 原因
	 */
	private String reason;
}
