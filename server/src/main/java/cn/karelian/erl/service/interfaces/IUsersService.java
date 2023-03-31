package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.AsignRoleParam;
import cn.karelian.erl.DataTransferObject.AuthorizeParam;
import cn.karelian.erl.DataTransferObject.VerifyCodeParam;
import cn.karelian.erl.DataTransferObject.RevisePasswordParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.Exceptions.UnLoginException;
import cn.karelian.erl.entity.Usermsgs;
import cn.karelian.erl.entity.Users;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理用户的表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface IUsersService extends IService<Users> {
	public Result getPaperAuthor(Long id);

	public Result selfindex() throws NullRequestException;

	public Result edit(Usermsgs usermsg);

	public Result selfedit(Usermsgs usermsg) throws NullRequestException;

	public Result reset(List<Long> ids);

	public Result authorize(AuthorizeParam params) throws TransactionFailedException;

	public Result authorizeindex(Long id, Boolean all) throws NullRequestException;

	public Result selfreset(RevisePasswordParam params) throws NullRequestException, UnLoginException;

	public Result asignindex(List<Long> ids);

	public Result asign(AsignRoleParam params) throws NullRequestException, TransactionFailedException;

	public Result bind(VerifyCodeParam params) throws NullRequestException;

	public Result getverifies(String account) throws NullRequestException;
}
