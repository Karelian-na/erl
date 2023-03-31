package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.BookReservationParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Textbooks;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface ITextbooksService extends IService<Textbooks> {
	public Result resindex(IndexParam params) throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result reserve(BookReservationParam params) throws NullRequestException;

	public Result edit(Textbooks textbook);

	public Result add(Textbooks textbook);

	public Result delete(List<Integer> ids);
}
