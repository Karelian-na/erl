package cn.karelian.erl.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理专业的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
@TableName("majors")
public class Majors implements Serializable {


	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 学位点代码
	 */
	private String number;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String descrip;

	/**
	 * 添加人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String add_user;

	/**
	 * 添加时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime add_time;
}
