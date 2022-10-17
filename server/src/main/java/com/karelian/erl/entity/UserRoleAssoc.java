package com.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
@Getter
@Setter
  @TableName("user_role_assoc")
public class UserRoleAssoc implements Serializable {

    private static final long serialVersionUID = 1L;

      private Long uid;

      private Byte rid;
}
