package cn.karelian.erl.service;

import cn.karelian.erl.DataTransferObject.AuditParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.LaboratoryReservationParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.entity.LaboratoryReservations;
import cn.karelian.erl.mapper.LaboratoryReservationsMapper;
import cn.karelian.erl.mapper.view.LaboratoryReservationsViewMapper;
import cn.karelian.erl.service.interfaces.ILaboratoryReservationsService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.view.LaboratoryReservationsView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-02-15
 */
@Service
public class LaboratoryReservationsService extends ErlServiceImpl<LaboratoryReservationsMapper, LaboratoryReservations>
		implements ILaboratoryReservationsService {

	@Autowired
	private LaboratoryReservationsViewMapper viewMapper;

	public Result checkFields(LaboratoryReservationParam reservation, boolean add) {
		int code = EntityUtil.CheckStringField(reservation.message, 1, 100, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("message", code);
		}

		code = EntityUtil.CheckNumberField(reservation.day, LocalDate.now(), null, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("day", code);
		}

		code = EntityUtil.CheckNumberField(reservation.start_time, LocalTime.of(8, 20), null, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("start_time", code);
		}

		code = EntityUtil.CheckNumberField(reservation.end_time, reservation.start_time, LocalTime.of(18, 0), add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("end_time", code);
		}

		return null;
	}

	@Override
	public Result index(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<LaboratoryReservationsView> qw = new QueryWrapper<>();
		qw.orderBy(true, false, "add_time");
		return super.index(viewMapper, params, qw);
	}

	@Override
	public Result audit(AuditParam params) throws NullRequestException {
		if (params.audit_status > 2 || params.audit_status < 1
				|| (params.comment != null && params.comment.length() > 100)) {
			return Result.InvalidArgument;
		}

		LambdaUpdateWrapper<LaboratoryReservations> luw = new LambdaUpdateWrapper<>();
		luw.eq(LaboratoryReservations::getAudit_status, 0);
		luw.in(LaboratoryReservations::getId, params.ids);

		LaboratoryReservations reservation = new LaboratoryReservations();
		reservation.setComment(params.comment);
		reservation.setAudit_status(params.audit_status);
		reservation.setAudit_time(LocalDateTime.now());
		reservation.setAudit_user(LoginInfomationUtil.getUserName());

		Result result = new Result(super.update(reservation, luw));
		if (result.isSuccess() && params.ids.size() == 1) {
			result.setData(EntityUtil.ToMap(reservation));
		}
		return result;
	}

}
