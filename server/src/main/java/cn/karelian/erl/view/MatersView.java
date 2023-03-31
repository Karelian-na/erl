package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;

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
@TableName("maters_view")
public class MatersView implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 分组
	 */
	@JsonIgnore
	@TableField(value = "`group`", select = false)
	private String group;

	/**
	 * 类型
	 */
	@JsonIgnore
	@TableField(select = false)
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
	 * 上传人
	 */
	private String upload_user;

	/**
	 * 上传时间
	 */
	private LocalDate upload_time;

	/**
	 * 下载次数
	 */
	private Integer download_times;
}
