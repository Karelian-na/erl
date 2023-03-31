package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.entity.Patents;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.mapper.ErlMapper;
import cn.karelian.erl.mapper.PatentsMapper;
import cn.karelian.erl.mapper.view.PatentsViewMapper;
import cn.karelian.erl.service.interfaces.IDeclarableService;
import cn.karelian.erl.service.interfaces.IPatentsService;
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
public class PatentsService extends ErlServiceImpl<PatentsMapper, Patents>
		implements IPatentsService, IDeclarableService<Patents> {
	@Autowired
	private PatentsViewMapper viewMapper;

	@Override
	public ErlMapper<?> getViewMapper() {
		return viewMapper;
	}

	@Override
	public Result declare(Patents entity) {
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
	public Result delete(List<Integer> ids) {
		if (ids.size() == 0) {
			return Result.fieldError("ids", FieldError.EMPTY);
		}

		Result result = new Result();
		QueryWrapper<Patents> qw = new QueryWrapper<>();
		qw.in("id", ids).and(t -> t.notExists(
				"SELECT 1 FROM declarations d WHERE d.`group` = 'Patent' AND d.ref_id = patents.id AND d.audit_status <> 1"));
		result.setSuccess(super.remove(qw));

		return result;
	}
}
