package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 论文作者关联表
 * </p>
 *
 * @author baomidou
 * @since 2023-02-22
 */
@Getter
@Setter
@TableName("paper_author_assoc")
public class PaperAuthorAssoc implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer id;

	private Integer pid;

	private Long uid;

	private String name;

	@TableField(value = "`order`")
	private Byte order;

	private String role;

	private Boolean is_corresponding;
}
