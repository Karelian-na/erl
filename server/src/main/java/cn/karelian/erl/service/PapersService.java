package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Disciplines;
import cn.karelian.erl.entity.Papers;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.mapper.PapersMapper;
import cn.karelian.erl.mapper.view.PapersAuthorViewMapper;
import cn.karelian.erl.mapper.view.PapersViewMapper;
import cn.karelian.erl.service.interfaces.IDeclarableService;
import cn.karelian.erl.service.interfaces.IPapersService;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.WebPageInfo;
import cn.karelian.erl.view.FieldsInfoView;
import cn.karelian.erl.view.PapersAuthorView;
import cn.karelian.erl.view.PapersView;
import lombok.Getter;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 管理论文的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
@Service
@Getter
public class PapersService extends ErlServiceImpl<PapersMapper, Papers>
		implements IPapersService, IDeclarableService<Papers> {
	@Autowired
	public PapersViewMapper viewMapper;
	@Autowired
	private DisciplinesService disciplinesService;
	@Autowired
	private PaperAuthorAssocService paperAuthorAssocService;
	@Autowired
	private PapersAuthorViewMapper authorViewMapper;

	@Override
	public Result index(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<PapersView> qw = new QueryWrapper<>();
		qw.eq("audit_status", 1);
		return ErlServiceImpl.index(viewMapper, params, qw);
	}

	@Override
	public Result postindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<PapersAuthorView> qw = new QueryWrapper<>();
		qw.eq("uid", LoginInfomationUtil.getUserId());
		Result result = ErlServiceImpl.index(authorViewMapper, params, qw);
		if (params.initPageSize != null) {
			WebPageInfo<?> info = result.getData(WebPageInfo.class);
			info.fields.removeIf(t -> t.getField_name().equals("audit_status"));
		}
		return result;
	}

	@Override
	public Map<String, Object> declareindex(List<FieldsInfoView> addFields) throws IllegalAccessException {
		Map<String, Object> data = new HashMap<>();
		LambdaQueryWrapper<Disciplines> lqw = new LambdaQueryWrapper<>();
		lqw.select(Disciplines::getId, Disciplines::getName);
		data.put("disciplines", disciplinesService.list(lqw));

		List<FieldsInfoView> fields = ErlServiceImpl.getFields(PapersView.class);
		fields.remove(fields.size() - 1);
		fields.forEach(t -> t.setEditable(false));
		fields.addAll(addFields);
		data.put("fields", fields);
		return data;
	}

	@Override
	public Result declare(Papers paper) {
		Result result = new Result();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			String unique = paper.getTitle() + paper.getPub_jour() + paper.getPub_year()
					+ paper.getPub_term() + paper.getPub_vol();
			digest.update(unique.getBytes());
			paper.setUnique_id(HexUtils.toHexString(digest.digest()));
		} catch (Exception e) {
			return Result.internalError(null);
		}
		if (super.lambdaQuery().eq(Papers::getUnique_id, paper.getUnique_id()).exists()) {
			result.setMsg("论文已存在!");
			return result;
		}

		if (!super.save(paper)) {
			result.setMsg("论文保存失败!");
			return result;
		}

		paper.getAuthors().forEach(t -> t.setPid(paper.getId()));
		result.setSuccess(paperAuthorAssocService.saveBatch(paper.getAuthors()));
		return result;
	}

	@Override
	public Result delete(List<Integer> ids) {
		if (ids.size() == 0) {
			return Result.fieldError("ids", FieldError.EMPTY);
		}

		Result result = new Result();
		QueryWrapper<Papers> qw = new QueryWrapper<>();
		qw.in("id", ids).and(t -> t.notExists(
				"SELECT 1 FROM declarations d WHERE d.`group` = 'Paper' AND d.ref_id = academic_papers.id AND d.audit_status <> 1"));
		result.setSuccess(super.remove(qw));

		return result;
	}
}
