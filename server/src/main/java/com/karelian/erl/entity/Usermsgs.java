package com.karelian.erl.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理用户基本信息的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-15
 */
@Getter
@Setter
  public class Usermsgs implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * ID
     */
        private Long id;

      /**
     * 姓名
     */
      private String name;

      /**
     * 性别
     */
      private String gender;

      /**
     * 电子邮箱
     */
      private String email;

      /**
     * 联系方式
     */
      private String phone;

      /**
     * 政治面貌
     */
      private String politicalStatus;

      /**
     * 民族
     */
      private String clan;
}
