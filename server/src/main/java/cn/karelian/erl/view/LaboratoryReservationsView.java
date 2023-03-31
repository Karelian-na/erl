package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Karelian_na
 * @since 2023-02-28
 */
@Getter
@Setter
  @TableName("laboratory_reservations_view")
public class LaboratoryReservationsView implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * ID
     */
      private Integer id;

      /**
     * 实验室编号
     */
      private String number;

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
      private String commander;

      /**
     * 姓名
     */
      private String add_user;

      /**
     * 预约日期
     */
      private LocalDate day;

      /**
     * 开始时间
     */
      private LocalTime start;

      /**
     * 结束时间
     */
      private LocalTime end;

      /**
     * 留言
     */
      private String message;

      /**
     * 预约时间
     */
      private LocalDateTime add_time;

      /**
     * 审核状态
     */
      private Byte audit_status;

      /**
     * 审核时间
     */
      private LocalDateTime audit_time;

      /**
     * 审核人
     */
      private String audit_user;

      /**
     * 意见
     */
      private String comment;
}
