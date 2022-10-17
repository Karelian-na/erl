package com.karelian.erl.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理用户的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
  public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * ID
     */
        private Long id;

      /**
     * 密码
     */
      private String pwd;

      /**
     * 最后登录IP
     */
      private String lastLoginIp;

      /**
     * 最后登录session
     */
      private String lastLoginSession;

      /**
     * 最后登录时间
     */
      private LocalDateTime lastLoginDate;

      /**
     * 注册时间
     */
      private LocalDateTime regDate;
}
