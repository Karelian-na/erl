package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-02-28
 */
@Getter
@Setter
@TableName("views_info")
public class ViewsInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 视图名称
	 */
	@TableId
	private String view_name;

	/**
	 * 备注
	 */
	private String comment;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime update_time;

	/**
	 * 更新人
	 */
	@TableField(fill = FieldFill.UPDATE)
	private String update_user;
}
