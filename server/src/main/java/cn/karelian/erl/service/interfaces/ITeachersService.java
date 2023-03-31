package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Teachers;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理教师信息的表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface ITeachersService extends IService<Teachers> {
	public Result teacherindex(IndexParam params) throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result tutorindex(IndexParam params) throws IllegalAccessException, NullRequestException, PermissionNotFoundException;
	
	public Result add(List<Teachers> students);

	public Result edit(Teachers student);

	public Result delete(List<Long> ids);
}
