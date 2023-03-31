package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Projects;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.mapper.ErlMapper;
import cn.karelian.erl.mapper.ProjectsMapper;
import cn.karelian.erl.mapper.view.ProjectsViewMapper;
import cn.karelian.erl.service.interfaces.IDeclarableService;
import cn.karelian.erl.service.interfaces.IProjectsService;
import cn.karelian.erl.utils.ProjectType;
import cn.karelian.erl.view.FieldsInfoView;
import cn.karelian.erl.view.ProjectsView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-15
 */
@Service
public class ProjectsService extends ErlServiceImpl<ProjectsMapper, Projects>
		implements IProjectsService, IDeclarableService<Projects> {
	@Autowired
	private ProjectsViewMapper viewMapper;

	@Override
	public ErlMapper<?> getViewMapper() {
		return viewMapper;
	}

	public Result index(ProjectType type, IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<ProjectsView> qw = Wrappers.query();
		qw.eq("category", type.getValue());
		return ErlServiceImpl.index(viewMapper, params, qw);
	}

	@Override
	public Result declare(Projects entity) {
		Result result = new Result();
		result.setSuccess(super.save(entity));
		return result;
	}

	@Override
	public Map<String, Object> declareindex(List<FieldsInfoView> addFields) throws IllegalAccessException {
		Map<String, Object> data = new HashMap<>();
		List<FieldsInfoView> fields = super.getFields();
		fields.forEach(t -> t.setEditable(false));
		fields.addAll(addFields);
		data.put("fields", fields);
		return data;
	}

	@Override
	public Result delete(ProjectType type, List<Integer> ids) {
		if (ids.size() == 0) {
			return Result.fieldError("ids", FieldError.EMPTY);
		}

		Result result = new Result();
		QueryWrapper<Projects> qw = new QueryWrapper<>();
		qw.eq("category", type.getValue());
		qw.in("id", ids).and(t -> t.notExists(
				"SELECT 1 FROM declarations d WHERE d.`group` = 'Project' AND d.ref_id = projects.id AND d.audit_status <> 1"));
		result.setSuccess(super.remove(qw));

		return result;
	}
}
