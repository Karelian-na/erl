package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Teachers;
import cn.karelian.erl.mapper.ErlMapper;
import cn.karelian.erl.mapper.TeachersMapper;
import cn.karelian.erl.mapper.view.TeacmsgsViewMapper;
import cn.karelian.erl.mapper.view.TutmsgsViewMapper;
import cn.karelian.erl.service.interfaces.ITeachersService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理教师信息的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class TeachersService extends ErlServiceImpl<TeachersMapper, Teachers> implements ITeachersService {
	@Autowired
	private TutmsgsViewMapper tutmsgsViewMapper;
	@Autowired
	private TeacmsgsViewMapper teacmsgsViewMapper;

	@Override
	public ErlMapper<?> getViewMapper() {
		return teacmsgsViewMapper;
	}

	@Override
	public Result teacherindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return super.index(params);
	}

	@Override
	public Result tutorindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return ErlServiceImpl.index(tutmsgsViewMapper, params, null);
	}

	@Override
	public Result add(List<Teachers> students) {
		Result result = new Result();
		return result;
	}

	@Override
	public Result edit(Teachers student) {
		Result result = new Result();
		return result;
	}

	@Override
	public Result delete(List<Long> ids) {
		Result result = new Result();
		return result;
	}
}
