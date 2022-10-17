/*
 * @Author: Karelian_na
 */
package com.karelian.erl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 管理权限目录的表
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Getter
@Setter
  public class Permissions implements Serializable {

    private static final long serialVersionUID = 1L;
	public static final int TYPE_MENU = 1;
	public static final int TYPE_ITEM = 2;
	public static final int TYPE_PAGE = 3;
	public static final int TYPE_OPER = 4;

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
     * 图标
     */
      private String icon;

      /**
     * 类型
     */
      private Byte type;

      /**
     * 状态
     */
      private Boolean status;

      /**
     * 地址
     */
      private String url;

      /**
     * 备注
     */
      private String descrip;

      /**
     * 父权限ID
     */
      private Integer pid;

      /**
     * 创建人
     */
      private String createUser;

      /**
     * 创建时间
     */
      private LocalDateTime createTime;

      /**
     * 更新时间
     */
      private LocalDateTime updateTime;
}
