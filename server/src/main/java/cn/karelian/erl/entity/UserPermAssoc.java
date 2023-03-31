/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户权限关联表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
@Getter
@Setter
@TableName("user_perm_assoc")
public class UserPermAssoc implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long uid;

	private Integer pid;

	private Byte authorize;
}
