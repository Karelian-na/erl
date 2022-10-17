package com.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
  public class Internships implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * ID
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 名称
     */
      private String name;

      /**
     * 实习单位名称
     */
      private String unitName;

      /**
     * 实习单位简介
     */
      private String unitDescrip;

      /**
     * 实习时长
     */
      private Byte days;

      /**
     * 最大申请人数
     */
      private Integer maxAppNum;

      /**
     * 已申请人数
     */
      private Integer appNum;

      /**
     * 发布时间
     */
      private LocalDateTime publishTime;

      /**
     * 发布者
     */
      private String publishUser;

      /**
     * 申请截止时间
     */
      private LocalDateTime deadline;
}
