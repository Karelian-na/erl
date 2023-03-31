package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Courses;

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
public interface ICoursesService extends IService<Courses> {
	public Result expindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result expadd(Courses course);

	public Result add(Courses course);

	public Result expedit(Courses course);

	public Result edit(Courses course);

	public Result delete(List<Integer> ids);
}
