package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.AuditParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.BookReservations;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-11-15
 */
public interface IBookReservationsService extends IService<BookReservations> {
	public Result selfindex(IndexParam params)
			throws NullRequestException, IllegalAccessException, PermissionNotFoundException;

	public Result audit(AuditParam params) throws NullRequestException;

	public Result selfdelete(List<Integer> ids) throws NullRequestException;
}