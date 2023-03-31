package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.AuditParam;
import cn.karelian.erl.DataTransferObject.DeclareParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.entity.Declarations;
import cn.karelian.erl.view.DeclarationsView;
import cn.karelian.erl.view.FieldsInfoView;
import cn.karelian.erl.mapper.DeclarationsMapper;
import cn.karelian.erl.mapper.ErlMapper;
import cn.karelian.erl.mapper.view.DeclarationsViewMapper;
import cn.karelian.erl.service.interfaces.IDeclarableService;
import cn.karelian.erl.service.interfaces.IDeclarationsService;
import cn.karelian.erl.service.interfaces.IErlService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.Utils;
import cn.karelian.erl.utils.Utils.FileCategory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 管理各种申报的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-02-04
 */
@Service
public class DeclarationsService extends ErlServiceImpl<DeclarationsMapper, Declarations>
		implements IDeclarationsService {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PatentsService patentsService;
	@Autowired
	private ProjectsService projectsService;
	@Autowired
	private PapersService papersService;
	@Autowired
	private TeachingAwardsService teachingAwardsService;
	@Autowired
	private ConferencesService conferencesService;
	@Autowired
	private CompetitionAwardsService competitionAwardsService;
	@Autowired
	private DeclarationsViewMapper viewMapper;

	@Override
	public ErlMapper<?> getViewMapper() {
		return viewMapper;
	}

	@Override
	public IDeclarableService<?> getServiceByType(String type) {
		IDeclarableService<?> services = null;
		switch (type) {
			case "Teaching":
				services = teachingAwardsService;
				break;
			case "Competition":
				services = competitionAwardsService;
				break;
			case "Teacher":
				services = competitionAwardsService;
				break;
			case "Patent":
				services = patentsService;
				break;
			case "Paper":
				services = papersService;
				break;
			case "Conference":
				services = conferencesService;
				break;
			case "Project":
				services = projectsService;
				break;
			default:
				break;
		}
		return services;
	}

	@Override
	public Result index(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<DeclarationsView> qw = new QueryWrapper<>();
		qw.orderBy(true, false, "add_time");

		return super.index(viewMapper, params, qw);
	}

	@Override
	@Transactional(rollbackFor = TransactionFailedException.class)
	@SuppressWarnings("unchecked")
	public Result declare(String type, DeclareParam params) throws TransactionFailedException {

		Result result = new Result(false);

		if (!ObjectUtils.isEmpty(params.enclosures)) {
			List<String> urls = new ArrayList<>();
			String[] enclosures = params.enclosures.split(";");
			for (String enclosureName : enclosures) {
				String url = Utils.CopyTempFileToSpecifiedCategory(FileCategory.ENCLOSURE, enclosureName);
				if (url == null) {
					result.setMsg("附件保存失败!");
					return result;
				}
				urls.add(url);
			}
			params.enclosures = String.join(";", urls);
		}

		String group = type.equals("Award") ? params.awardType : type;

		IDeclarableService<?> services = getServiceByType(group);
		if (services != null) {
			Class<?> clszz = services.getEntityClass();

			Object entity = null;
			try {
				entity = objectMapper.readValue(params.jsonData, clszz);
			} catch (JsonProcessingException e) {
				return Result.InvalidArgument;
			}

			try {
				Field field = clszz.getDeclaredField("enclosures");
				field.setAccessible(true);
				field.set(entity, params.enclosures);
				result = ((IDeclarableService<Object>) services).declare(entity);

				if (!result.isSuccess()) {
					throw new TransactionFailedException(result);
				}

				field = clszz.getDeclaredField("id");
				field.setAccessible(true);
				Integer id = Integer.class.cast(field.get(entity));

				Declarations declaration = new Declarations();
				declaration.setMessage(params.message);
				declaration.setRef_id(id);
				declaration.setGroup(group);
				result.setSuccess(this.save(declaration));
				if (!result.isSuccess()) {
					throw new TransactionFailedException(result);
				}

			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			}
		}

		return result;
	}

	@Override
	public Result auditindex(String group, Integer refId) {
		IErlService<?> service = getServiceByType(group);
		if (service == null) {
			return Result.InvalidArgument;
		}

		Object data = null;
		if (group.equals("Paper")) {
			data = ((PapersService) service).getViewMapper().selectById(refId);
		} else {
			data = service.getById(refId);
		}
		return new Result(data != null, data);
	}

	@Override
	public Result audit(AuditParam params) throws NullRequestException {
		if (params.audit_status > 2 || params.audit_status < 1
				|| (params.comment != null && params.comment.length() > 100)) {
			return Result.InvalidArgument;
		}

		LambdaUpdateWrapper<Declarations> luw = new LambdaUpdateWrapper<>();
		luw.eq(Declarations::getAudit_status, 0);
		luw.in(Declarations::getId, params.ids);

		Declarations declaration = new Declarations();
		declaration.setComment(params.comment);
		declaration.setAudit_status(params.audit_status);
		declaration.setAudit_time(LocalDateTime.now());
		declaration.setAudit_user(LoginInfomationUtil.getUserName());

		Result result = new Result(super.update(declaration, luw));
		if (result.isSuccess()) {
			result.setData(EntityUtil.ToMap(declaration));
		}
		return result;
	}

	@Override
	public Result declareindex(String type) throws IllegalAccessException {
		Result result = new Result(true);

		QueryWrapper<DeclarationsView> qw = new QueryWrapper<>();
		qw.select("message");
		List<FieldsInfoView> addFields = super.getFields(DeclarationsView.class, qw);
		addFields.forEach(u -> {
			u.setDisplay(false);
			u.setEditable(true);
		});

		Map<String, Object> data = null;
		switch (type) {
			case "Award": {
				data = competitionAwardsService.declareindex(addFields);
				data.putAll(teachingAwardsService.declareindex(addFields));
				break;
			}
			default:
				IDeclarableService<?> service = getServiceByType(type);
				if (service == null) {
					return Result.paramError("type");
				}
				data = service.declareindex(addFields);
				break;
		}
		result.setData(data.size() == 1 ? data.values().toArray()[0] : data);
		return result;
	}
}
