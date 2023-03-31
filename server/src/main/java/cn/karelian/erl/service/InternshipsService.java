package cn.karelian.erl.service;

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.InternshipManageParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.entity.Applications;
import cn.karelian.erl.entity.Internships;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.mapper.ApplicationsMapper;
import cn.karelian.erl.mapper.InternshipsMapper;
import cn.karelian.erl.mapper.view.InternshipAppsViewMapper;
import cn.karelian.erl.service.interfaces.IInternshipsService;
import cn.karelian.erl.utils.ApplicationStatus;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.InternshipStatus;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.OperButton;
import cn.karelian.erl.utils.PageData;
import cn.karelian.erl.utils.WebPageInfo;
import cn.karelian.erl.view.InternshipAppsView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class InternshipsService extends ErlServiceImpl<InternshipsMapper, Internships> implements IInternshipsService {
	@Autowired
	private ApplicationsMapper applicationsMapper;
	@Autowired
	private InternshipAppsViewMapper internshipAppsViewMapper;

	private Result checkFields(Internships internship, boolean add) {
		int code = EntityUtil.CheckStringField(internship.getName(), 5, 50, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("name", code);
		}

		code = EntityUtil.CheckStringField(internship.getUnit_name(), 5, 50, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("unit_name", code);
		}

		code = EntityUtil.CheckStringField(internship.getUnit_descrip(), 0, 100, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("unit_descrip", code);
		}

		code = EntityUtil.CheckNumberField(internship.getStart_date(), LocalDate.now(), LocalDate.now().plusDays(62),
				add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("start_date", code);
		}

		code = EntityUtil.CheckNumberField(internship.getDays(), (short) 15, (short) 180, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("days", code);
		}

		code = EntityUtil.CheckNumberField(internship.getMax_app_num(), (short) 2, (short) 100, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("max_app_num", code);
		}

		code = EntityUtil.CheckNumberField(internship.getDeadline(), internship.getStart_date(),
				internship.getStart_date().plusDays(internship.getDays()), add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("deadline", code);
		}

		code = EntityUtil.CheckStringField(internship.getDescrip(), 0, 100, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("descrip", code);
		}
		return null;
	}

	@Override
	public Result appindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return super.index(params);
	}

	@Override
	public Result add(Internships internship) {
		Result result = checkFields(internship, true);
		if (result != null)
			return result;

		result = new Result(super.save(internship));
		if (result.isSuccess()) {
			result.setData(internship);
		}
		return result;
	}

	@Override
	public Result edit(Internships internship) {
		if (internship.getId() == null) {
			return Result.paramError("id");
		}

		Result result = checkFields(internship, false);
		if (result != null)
			return result;

		result = new Result();
		if (super.lambdaQuery().eq(Internships::getId, internship.getId()).exists()) {
			result.setMsg("任务不存在!");
			return result;
		}

		result.setSuccess(super.lambdaUpdate()
				.eq(Internships::getStatus, InternshipStatus.Recruiting.getValue())
				.update(internship));

		if (result.isSuccess()) {
			result.setData(internship);
		}
		return result;
	}

	@Override
	public Result delete(List<Integer> ids) {
		if (ids.size() != 1) {
			return Result.paramError("ids");
		}

		LambdaQueryWrapper<Internships> lqw = Wrappers.lambdaQuery();
		lqw.eq(Internships::getId, ids.get(0))
				.ne(Internships::getStatus, InternshipStatus.InProgress.getValue());
		return new Result(super.remove(lqw));
	}

	@Override
	@Transactional(rollbackFor = TransactionFailedException.class)
	public Result apply(Integer id) throws TransactionFailedException, NullRequestException {
		if (id == null) {
			return Result.paramError("id");
		}

		Result result = new Result();
		Internships internship = super.lambdaQuery().select(Internships::getMax_app_num, Internships::getApp_num)
				.eq(Internships::getId, id).one();
		if (internship == null) {
			result.setMsg("实习任务不存在!");
			return result;
		}

		Short appNum = internship.getApp_num();
		if (appNum == internship.getMax_app_num()) {
			result.setMsg("申请人数已满!");
			return result;
		}

		LambdaQueryWrapper<Applications> alqw = new LambdaQueryWrapper<>();
		alqw.eq(Applications::getIid, id)
				.eq(Applications::getAdd_uid, LoginInfomationUtil.getUserId());
		if (applicationsMapper.exists(alqw)) {
			result.setMsg("不可重复申请!");
			return result;
		}

		if (!super.lambdaUpdate()
				.set(Internships::getApp_num, appNum + 1)
				.eq(Internships::getId, id)
				.eq(Internships::getApp_num, appNum)
				.update()) {
			result.setMsg("状态不一致!");
			return result;
		}

		Applications application = new Applications();
		application.setIid(id);
		result.setSuccess(applicationsMapper.insert(application) != 0);

		if (!result.isSuccess()) {
			throw new TransactionFailedException(result);
		}

		result.setData(appNum + 1);
		return result;
	}

	@Override
	@Transactional(rollbackFor = TransactionFailedException.class)
	public Result withdraw(Integer id) throws NullRequestException, TransactionFailedException {
		if (id == null) {
			return Result.paramError("id");
		}

		Result result = new Result();
		Internships internship = super.lambdaQuery()
				.select(Internships::getApp_num)
				.eq(Internships::getId, id)
				.one();
		if (internship == null) {
			result.setMsg("实习任务不存在!");
			return result;
		}

		LambdaQueryWrapper<Applications> alqw = new LambdaQueryWrapper<>();
		alqw.select(Applications::getId)
				.eq(Applications::getIid, id)
				.eq(Applications::getAdd_uid, LoginInfomationUtil.getUserId());
		Applications application = applicationsMapper.selectOne(alqw);
		if (application == null) {
			result.setMsg("未申请该实习任务!");
			return result;
		}

		Short appNum = internship.getApp_num();
		if (!super.lambdaUpdate()
				.set(Internships::getApp_num, appNum - 1)
				.eq(Internships::getId, id)
				.eq(Internships::getApp_num, appNum)
				.update()) {
			result.setMsg("状态不一致!");
			return result;
		}

		result.setSuccess(applicationsMapper.deleteById(application) != 0);

		if (!result.isSuccess()) {
			throw new TransactionFailedException(result);
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result manageindex(Integer iid, IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<InternshipAppsView> qw = new QueryWrapper<>();
		qw.eq("iid", iid).orderByAsc("app_status");
		if (params.initPageSize != null) {
			Result result;
			WebPageInfo<InternshipAppsView> webPageInfo = new WebPageInfo<>();
			webPageInfo.fields = ErlServiceImpl.getFields(InternshipAppsView.class);

			params.pageIdx = 1L;
			params.pageSize = params.initPageSize;
			result = ErlServiceImpl.getPageData(internshipAppsViewMapper, params, qw);
			webPageInfo.operButtons = List.of(OperButton.ofDelete("撤销", 2));
			webPageInfo.pageData = PageData.class.cast(result.getData());

			result.setData(webPageInfo);
			return result;
		} else {
			return ErlServiceImpl.index(internshipAppsViewMapper, params, qw);
		}
	}

	@Override
	@Transactional(rollbackFor = TransactionFailedException.class)
	public Result manage(Integer iid, InternshipManageParam params) throws TransactionFailedException {
		if (iid == null) {
			return Result.fieldError("iid", FieldError.EMPTY);
		}

		if (params.status != null) {
			LambdaUpdateWrapper<Internships> luw = Wrappers.lambdaUpdate();
			LambdaUpdateWrapper<Applications> iluw = Wrappers.lambdaUpdate();

			if (params.status == InternshipStatus.InProgress.getValue()) {
				luw.eq(Internships::getStatus, InternshipStatus.Recruiting.getValue());
				iluw.set(Applications::getStatus, ApplicationStatus.InProgress.getValue());
			} else if (params.status == InternshipStatus.Canceled.getValue()) {
				luw.eq(Internships::getStatus, InternshipStatus.Recruiting.getValue());
				iluw.set(Applications::getStatus, ApplicationStatus.Canceled.getValue());
			} else {
				return Result.fieldError("status", FieldError.TOO_LONG);
			}

			luw.set(Internships::getStatus, params.status).eq(Internships::getId, iid);

			Result result = new Result();
			result.setSuccess(super.update(luw));
			if (result.isSuccess()) {
				iluw.eq(Applications::getIid, iid)
						.eq(Applications::getStatus, ApplicationStatus.Applied.getValue());
				result.setSuccess(applicationsMapper.update(null, iluw) != 0);

				if (!result.isSuccess()) {
					throw new TransactionFailedException(result);
				}
			}

			return result;
		} else if (params.appStatus != null) {
			if (params.ids == null || params.ids.size() == 0) {
				return Result.fieldError("ids", FieldError.EMPTY);
			}

			if (params.appStatus != ApplicationStatus.beenRevoked.getValue()) {
				return Result.fieldError("appStatus", FieldError.TOO_SMALL);
			}

			int code = EntityUtil.CheckStringField(params.reason, 0, 50, false);
			if (code != StatusCode.ERROR_SUCCESS) {
				return Result.fieldError("reason", code);
			}

			LambdaUpdateWrapper<Applications> luw = Wrappers.lambdaUpdate();
			luw.eq(Applications::getIid, iid);
			luw.in(Applications::getId, params.ids);

			Applications application = new Applications();
			application.setReason(params.reason);
			application.setStatus(params.appStatus);
			application.setOper_time(LocalDateTime.now());

			Result result = new Result(applicationsMapper.update(application, luw) != 0);
			if (result.isSuccess()) {
				InternshipAppsView view = new InternshipAppsView();
				view.setApp_status(params.appStatus);
				view.setOper_time(application.getOper_time());
				view.setReason(params.reason);
				result.setData(view);
			}
			return result;
		}

		return Result.fieldError("status | ids", FieldError.EMPTY);
	}
}
