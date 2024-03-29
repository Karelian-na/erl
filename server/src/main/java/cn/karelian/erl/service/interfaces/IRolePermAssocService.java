/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.entity.RolePermAssoc;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色权限关联表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
public interface IRolePermAssocService extends IService<RolePermAssoc> {
	public boolean isAuthorized(Integer pid, Byte rid);
}
