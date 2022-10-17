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
  public class Textbooks implements Serializable {

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
     * 封面
     */
      private String cover;

      /**
     * 类别
     */
      private String type;

      /**
     * 作者
     */
      private String author;

      /**
     * 出版社
     */
      private String publisher;

      /**
     * ISBN
     */
      private String isbn;

      /**
     * 价格
     */
      private Integer price;

      /**
     * 添加人
     */
      private String addUser;

      /**
     * 添加时间
     */
      private LocalDateTime addTime;
}
