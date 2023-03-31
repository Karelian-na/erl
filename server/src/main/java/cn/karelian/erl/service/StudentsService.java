package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Students;
import cn.karelian.erl.mapper.ErlMapper;
import cn.karelian.erl.mapper.StudentsMapper;
import cn.karelian.erl.mapper.view.PostmsgsViewMapper;
import cn.karelian.erl.mapper.view.StumsgsViewMapper;
import cn.karelian.erl.service.interfaces.IStudentsService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理学生信息的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class StudentsService extends ErlServiceImpl<StudentsMapper, Students> implements IStudentsService {
	@Autowired
	private StumsgsViewMapper stumsgsViewMapper;
	@Autowired
	private PostmsgsViewMapper postmsgsViewMapper;

	@Override
	public ErlMapper<?> getViewMapper() {
		return stumsgsViewMapper;
	}

	@Override
	public Result studentindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return super.index(stumsgsViewMapper, params);
	}

	@Override
	public Result postindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return ErlServiceImpl.index(postmsgsViewMapper, params);
	}

	@Override
	public Result add(List<Students> students) {
		Result result = new Result();
		return result;
	}

	@Override
	public Result edit(Students student) {
		Result result = new Result();
		return result;
	}

	@Override
	public Result delete(List<Long> ids) {
		Result result = new Result();
		return result;
	}
}
