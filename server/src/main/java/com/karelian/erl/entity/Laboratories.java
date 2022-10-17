package com.karelian.erl.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
  public class Laboratories implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * ID
     */
        private Integer id;

      /**
     * 名称
     */
      private String name;

      /**
     * 容量
     */
      private Byte capacity;

      /**
     * 位置
     */
      private String addr;

      /**
     * 负责人
     */
      private String principal;

      /**
     * 添加时间
     */
      private LocalDateTime addTime;
}
