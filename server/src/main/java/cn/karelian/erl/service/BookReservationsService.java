package cn.karelian.erl.service;

import cn.karelian.erl.DataTransferObject.AuditParam;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Result;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.BookReservations;
import cn.karelian.erl.mapper.BookReservationsMapper;
import cn.karelian.erl.mapper.ErlMapper;
import cn.karelian.erl.mapper.view.BookReservationsViewMapper;
import cn.karelian.erl.service.interfaces.IBookReservationsService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.LoginInfomationUtil;
import cn.karelian.erl.utils.WebPageInfo;
import cn.karelian.erl.view.BookReservationsView;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-11-15
 */
@Service
public class BookReservationsService extends ErlServiceImpl<BookReservationsMapper, BookReservations>
		implements IBookReservationsService {
	@Autowired
	private BookReservationsViewMapper viewMapper;

	@Override
	public ErlMapper<?> getViewMapper() {
		return viewMapper;
	}

	@Override
	public Result index(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return super.index(params);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result selfindex(IndexParam params)
			throws NullRequestException, IllegalAccessException, PermissionNotFoundException {
		QueryWrapper<BookReservationsView> qw = new QueryWrapper<>();
		qw.eq("uid", LoginInfomationUtil.getUserId());

		Result result = super.index(viewMapper, params, qw);
		if (params.initPageSize != null) {
			WebPageInfo<BookReservations> info = WebPageInfo.class.cast(result.getData());
			List<String> removes = List.of("uid", "add_user");
			info.fields.removeIf(t -> removes.indexOf(t.getField_name()) != -1);
		}
		return result;
	}

	@Override
	public Result audit(AuditParam params) {
		if (params.audit_status > 2 || params.audit_status < 1
				|| (params.comment != null && params.comment.length() > 100)) {
			return Result.InvalidArgument;
		}

		LambdaUpdateWrapper<BookReservations> luw = new LambdaUpdateWrapper<>();
		luw.eq(BookReservations::getAudit_status, 0);
		luw.in(BookReservations::getId, params.ids);

		BookReservations reservation = new BookReservations();
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

	@Override
	public Result selfdelete(List<Integer> ids) {
		LambdaQueryWrapper<BookReservations> lqw = new LambdaQueryWrapper<>();
		lqw.in(BookReservations::getId, ids);
		lqw.eq(BookReservations::getUid, LoginInfomationUtil.getUserId());
		return new Result(super.remove(lqw));
	}

}
