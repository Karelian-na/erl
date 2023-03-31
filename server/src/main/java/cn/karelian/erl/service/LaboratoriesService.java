package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.LaboratoryReservationParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Laboratories;
import cn.karelian.erl.entity.LaboratoryReservations;
import cn.karelian.erl.mapper.LaboratoriesMapper;
import cn.karelian.erl.service.interfaces.ILaboratoriesService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.WebPageInfo;
import cn.karelian.erl.view.FieldsInfoView;
import cn.karelian.erl.view.LaboratoryReservationsView;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class LaboratoriesService extends ErlServiceImpl<LaboratoriesMapper, Laboratories>
		implements ILaboratoriesService {

	@Autowired
	private LaboratoryReservationsService laboratoryReservationsService;

	private Result checkFields(Laboratories laboratory, boolean add) {
		int code = EntityUtil.CheckStringField(laboratory.getNumber(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("number", code);
		}

		code = EntityUtil.CheckStringField(laboratory.getName(), 1, 50, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("name", code);
		}

		code = EntityUtil.CheckNumberField(laboratory.getCapacity(), (byte) 1, (byte) 127, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("capacity", code);
		}

		code = EntityUtil.CheckStringField(laboratory.getAddr(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("addr", code);
		}

		code = EntityUtil.CheckStringField(laboratory.getCommander(), 1, 20, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("commander", code);
		}

		return null;
	}

	@Override
	public Result add(Laboratories laboratory) throws NullRequestException {
		Result result = this.checkFields(laboratory, true);
		if (result != null) {
			return result;
		}

		result = new Result();
		if (super.lambdaQuery().eq(Laboratories::getNumber, laboratory.getNumber()).exists()) {
			result.setMsg("编号为" + laboratory.getNumber() + "的实验室已存在!");
			return result;
		}

		result.setSuccess(super.save(laboratory));
		if (result.isSuccess()) {
			result.setData(laboratory);
		}
		return result;
	}

	@Override
	public Result edit(Laboratories laboratory) {
		if (laboratory.getId() == null) {
			return Result.emptyError("id");
		}

		Result result = this.checkFields(laboratory, false);
		if (result != null) {
			return result;
		}

		result = new Result();
		if (!super.lambdaQuery().eq(Laboratories::getId, laboratory.getId()).exists()) {
			result.setMsg("实验室不存在!");
			return result;
		}

		laboratory.setAdd_time(null);
		laboratory.setAdd_user(null);

		result.setSuccess(super.updateById(laboratory));
		if (result.isSuccess()) {
			result.setData(EntityUtil.ToMap(laboratory));
		}
		return result;
	}

	@Override
	public Result delete(List<Integer> ids) {
		if (ids.size() != 1) {
			return Result.paramError("ids");
		}

		return new Result(super.removeById(ids.get(0)));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result resindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<Laboratories> qw = new QueryWrapper<>();
		qw.select("id", "number", "name", "capacity", "addr", "commander");
		Result result = super.index(params, qw);

		if (params.initPageSize != null) {
			QueryWrapper<LaboratoryReservationsView> rqw = new QueryWrapper<>();
			rqw.clear();
			rqw.select("day", "start_time", "end_time", "message");
			List<FieldsInfoView> addFields = ErlServiceImpl.getFields(LaboratoryReservationsView.class, rqw);

			WebPageInfo<Laboratories> webPageInfo = WebPageInfo.class.cast(result.getData());
			webPageInfo.fields.forEach((u) -> u.setEditable(false));
			addFields.forEach((u) -> {
				u.setDisplay(false);
				u.setEditable(true);
				webPageInfo.fields.add(u);
			});
		}

		return result;
	}

	@Override
	public Result reserve(LaboratoryReservationParam params) {
		if (params.id == null) {
			return Result.emptyError("id");
		}

		Result result = laboratoryReservationsService.checkFields(params, true);
		if (result != null) {
			return result;
		}

		result = new Result();
		if (!super.lambdaQuery().eq(Laboratories::getId, params.id).exists()) {
			result.setMsg("实验室不存在!");
			return result;
		}

		LaboratoryReservations laboratoryReservation = new LaboratoryReservations();
		laboratoryReservation.setLab_id(params.id);
		laboratoryReservation.setDay(params.day);
		laboratoryReservation.setStart(params.start_time);
		laboratoryReservation.setEnd(params.end_time);
		laboratoryReservation.setMessage(params.message);

		result.setSuccess(laboratoryReservationsService.save(laboratoryReservation));
		return result;
	}

}
