package com.karelian.erl.entity;

import java.io.Serializable;
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
  public class Courses implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 课程代码
     */
        private Integer id;

      /**
     * 名称
     */
      private String name;

      /**
     * 开课单位
     */
      private String unit;

      /**
     * 学分
     */
      private Double credit;

      /**
     * 总学时
     */
      private Byte totalHours;

      /**
     * 考核方式
     */
      private Byte assMethod;

      /**
     * 修读性质
     */
      private Boolean nature;

      /**
     * 类型
     */
      private String type;
}
