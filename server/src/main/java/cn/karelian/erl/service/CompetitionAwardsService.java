package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.CompetitionAwards;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.mapper.CompetitionAwardsMapper;
import cn.karelian.erl.mapper.ErlMapper;
import cn.karelian.erl.mapper.view.CompetitionAwardsViewMapper;
import cn.karelian.erl.service.interfaces.ICompetitionAwardsService;
import cn.karelian.erl.service.interfaces.IDeclarableService;
import cn.karelian.erl.utils.CompetitionRole;
import cn.karelian.erl.view.CompetitionAwardsView;
import cn.karelian.erl.view.FieldsInfoView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
@Service
public class CompetitionAwardsService extends ErlServiceImpl<CompetitionAwardsMapper, CompetitionAwards>
		implements ICompetitionAwardsService, IDeclarableService<CompetitionAwards> {

	@Autowired
	private CompetitionAwardsViewMapper viewMapper;

	@Override
	public ErlMapper<?> getViewMapper() {
		return viewMapper;
	}

	public Result index(CompetitionRole role, IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<CompetitionAwardsView> qw = new QueryWrapper<>();
		qw.eq("role", role.getValue());
		return ErlServiceImpl.index(viewMapper, params, qw);
	}

	@Override
	public Boolean add(CompetitionAwards award) {
		return this.save(award);
	}

	@Override
	public Result declare(CompetitionAwards entity) {
		return new Result(super.save(entity));
	}

	@Override
	public Map<String, Object> declareindex(List<FieldsInfoView> addFields) throws IllegalAccessException {
		Map<String, Object> data = new HashMap<>();
		List<FieldsInfoView> fields = super.getFields();
		fields.forEach(t -> t.setEditable(false));
		fields.addAll(addFields);
		data.put("Competition", fields);
		data.put("Teacher", fields);
		return data;
	}

	@Override
	public Result delete(List<Integer> ids) {
		if (ids.size() == 0) {
			return Result.fieldError("ids", FieldError.EMPTY);
		}

		Result result = new Result();
		QueryWrapper<CompetitionAwards> qw = new QueryWrapper<>();
		qw.in("id", ids).and(t -> t.notExists(
				"SELECT 1 FROM declarations d WHERE (d.`group` = 'Competition' OR d.`group` = 'Teacher') AND d.ref_id = competition_awards.id AND d.audit_status <> 1"));
		result.setSuccess(super.remove(qw));
		return result;
	}
}
