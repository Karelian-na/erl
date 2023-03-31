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
 * 角色权限关联表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
@Getter
@Setter
@TableName("role_perm_assoc")
public class RolePermAssoc implements Serializable {

	private static final long serialVersionUID = 1L;

	private Byte rid;

	private Integer pid;
}
