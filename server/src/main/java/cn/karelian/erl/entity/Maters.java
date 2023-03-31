package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @author baomidou
 * @since 2022-11-17
 */
@Getter
@Setter
@TableName("teaching_maters")
public class Maters implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 分组
	 */
	@TableField("`group`")
	private String group;

	/**
	 * 类型
	 */
	private String category;

	/**
	 * 文件名
	 */
	private String file_name;

	/**
	 * 文件大小
	 */
	private Long file_size;

	/**
	 * 文件地址
	 */
	private String file_addr;

	/**
	 * 上传人ID
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long add_uid;

	/**
	 * 上传时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime add_time;

	/**
	 * 下载次数
	 */
	private Integer download_times;
}
