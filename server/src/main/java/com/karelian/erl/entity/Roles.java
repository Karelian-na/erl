package com.karelian.erl.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理角色的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
  public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 角色ID
     */
        private Byte id;

      /**
     * 角色名称
     */
      private String name;

      /**
     * 创建人
     */
      private String createUser;

      /**
     * 创建时间
     */
      private LocalDateTime createTime;

      /**
     * 更新时间
     */
      private LocalDateTime updateTime;

      /**
     * 备注
     */
      private String descrip;
}
