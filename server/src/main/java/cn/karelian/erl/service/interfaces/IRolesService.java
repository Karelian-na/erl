package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.DataTransferObject.AuthorizeParam;
import cn.karelian.erl.Exceptions.TransactionFailedException;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.karelian.erl.Result;
import cn.karelian.erl.entity.Roles;

/**
 * <p>
 * 管理角色的表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-02-23
 */
public interface IRolesService extends IService<Roles> {
	public Result add(Roles role);

	public Result edit(Roles role);

	public Result authorizeindex(Integer id, Boolean all);

	public Result authorize(AuthorizeParam params) throws TransactionFailedException;

	public Result delete(Integer id);
}
