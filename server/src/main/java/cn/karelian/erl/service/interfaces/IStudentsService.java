package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Result;
import cn.karelian.erl.entity.Students;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理学生信息的表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface IStudentsService extends IService<Students> {
	public Result studentindex(IndexParam params) throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result postindex(IndexParam params) throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result add(List<Students> students);

	public Result edit(Students student);

	public Result delete(List<Long> ids);
}
