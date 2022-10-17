package com.karelian.erl.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理专业的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
  public class Majors implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 专业代码
     */
        private Integer id;

      /**
     * 专业名称
     */
      private String name;

      /**
     * 备注
     */
      private String descip;
}
