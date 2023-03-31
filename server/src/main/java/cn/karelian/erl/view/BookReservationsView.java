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
@TableName("book_reservations_view")
public class BookReservationsView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 用户ID
	 */
	private Long uid;

	/**
	 * 教材ID
	 */
	private Integer tid;

	/**
	 * 预订时间
	 */
	private LocalDateTime add_time;

	/**
	 * 预订数量
	 */
	private Byte amount;

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
	 * 姓名
	 */
	private String add_user;

	/**
	 * 名称
	 */
	private String book_name;

	/**
	 * 封面
	 */
	private String cover;

	/**
	 * 类别
	 */
	private String type;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 出版社
	 */
	private String publisher;

	/**
	 * 是否删除
	 */
	@TableLogic
	@JsonIgnore
	@TableField(select = false)
	private Byte deleted;
}
