package cn.karelian.erl.view;

import java.io.Serializable;
import java.time.LocalDateTime;
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
  public class Views implements Serializable {

    private static final long serialVersionUID = 1L;

    private String view_name;

      /**
     * 备注
     */
      private String comment;

      /**
     * 更新时间
     */
      private LocalDateTime update_time;

      /**
     * 更新人
     */
      private String update_user;
}
