package cn.karelian.erl.view;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
  @TableName("postmsgs_view")
public class PostmsgsView implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * ID/学籍号
     */
      private Long id;

      /**
     * 姓名
     */
      private String user_name;

      /**
     * 年龄
     */
      private Byte age;

      /**
     * 性别
     */
      private Byte gender;

      /**
     * 电子邮箱
     */
      private String email;

      /**
     * 联系方式
     */
      private String phone;

      /**
     * 专业名称
     */
      private String major_name;

      /**
     * 所在年级
     */
      private Short session;

      /**
     * 姓名
     */
      private String tutor_name;
}
