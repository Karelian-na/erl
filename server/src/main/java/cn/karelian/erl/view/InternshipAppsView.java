package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-03-13
 */
@Getter
@Setter
@TableName("internship_apps_view")
public class InternshipAppsView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Integer id;

	/**
	 * ID
	 */
	private Long add_uid;

	/**
	 * 姓名
	 */
	private String add_user;

	/**
	 * 申请时间
	 */
	private LocalDateTime add_time;

	/**
	 * 角色
	 */
	private String roles;

	/**
	 * 状态
	 */
	private Byte app_status;

	/**
	 * 状态
	 */
	private LocalDateTime oper_time;

	/**
	 * 原因
	 */
	private String reason;
}
