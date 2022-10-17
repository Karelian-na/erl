package com.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
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
  public class Applications implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 申请编号
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 实习任务ID
     */
      private Integer iid;

      /**
     * 实习人ID
     */
      private Long uid;

      /**
     * 状态
     */
      private Byte status;

      /**
     * 实习开始时间
     */
      private LocalDate startTime;
}
