package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.InternshipManageParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.entity.Internships;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface IInternshipsService extends IService<Internships> {
	public Result appindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result add(Internships internship);

	public Result edit(Internships internship);

	public Result delete(List<Integer> ids);

	public Result apply(Integer id) throws TransactionFailedException, NullRequestException;

	public Result withdraw(Integer id) throws NullRequestException, TransactionFailedException;

	public Result manageindex(Integer iid, IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result manage(Integer iid, InternshipManageParam params) throws TransactionFailedException;
}
