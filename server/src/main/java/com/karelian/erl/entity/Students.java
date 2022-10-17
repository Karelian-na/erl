package com.karelian.erl.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理学生信息的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
  public class Students implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * ID/学籍号
     */
        private Long id;

      /**
     * 专业ID
     */
      private Integer mid;

      /**
     * 班级
     */
      private Short className;

      /**
     * 所在年级
     */
      private Short session;

      /**
     * 学历
     */
      private String degree;

      /**
     * 导师ID
     */
      private Long tid;
}
