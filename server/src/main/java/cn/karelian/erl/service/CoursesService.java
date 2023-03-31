package cn.karelian.erl.service;

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.entity.Courses;
import cn.karelian.erl.mapper.CoursesMapper;
import cn.karelian.erl.service.interfaces.ICoursesService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.WebPageInfo;

import java.util.List;

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
public class CoursesService extends ErlServiceImpl<CoursesMapper, Courses> implements ICoursesService {
	private Result checkFields(Courses course, boolean add) {
		int code = EntityUtil.CheckStringField(course.getNumber(), 1, 10, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("number", code);
		}

		code = EntityUtil.CheckStringField(course.getName(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("name", code);
		}

		code = EntityUtil.CheckStringField(course.getUnit(), 1, 20, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("unit", code);
		}

		code = EntityUtil.CheckNumberField(course.getCredit(), 0F, 20F, add);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("credit", code);
		}

		code = EntityUtil.CheckNumberField(course.getTheo_hours(), (short) 0, (short) 200, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("theo_hours", code);
		}

		code = EntityUtil.CheckNumberField(course.getExp_hours(), (short) 0, (short) 200, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("exp_hours", code);
		}

		code = EntityUtil.CheckNumberField(course.getComp_hours(), (short) 0, (short) 200, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("comp_hours", code);
		}

		code = EntityUtil.CheckNumberField(course.getPrac_hours(), (short) 0, (short) 200, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("prac_hours", code);
		}

		code = EntityUtil.CheckNumberField(course.getAss_method(), (byte) 1, (byte) 2, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("ass_method", code);
		}

		code = EntityUtil.CheckNumberField(course.getNature(), (byte) 1, (byte) 2, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("nature", code);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result expindex(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		QueryWrapper<Courses> qw = new QueryWrapper<>();
		qw.eq("category", 3); // 实践教学模块

		Result result = super.index(params, qw);

		if (params.initPageSize != null) {
			WebPageInfo<Courses> webPageInfo = WebPageInfo.class.cast(result.getData());
			webPageInfo.fields.removeIf(t -> t.getField_name().equals("category"));
		}

		return result;
	}

	@Override
	public Result expadd(Courses course) {
		course.setCategory((byte) 3); // 实践教学模块
		return this.add(course);
	}

	@Override
	public Result add(Courses course) {
		Result result = this.checkFields(course, true);
		if (result != null) {
			return result;
		}

		result = new Result();
		if (super.lambdaQuery().eq(Courses::getNumber, course.getNumber()).exists()) {
			result.setMsg("编号为" + course.getNumber() + "的课程已存在!");
			return result;
		}

		result.setSuccess(super.save(course));
		if (result.isSuccess()) {
			result.setData(course);
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
	public Result expedit(Courses course) {
		course.setCategory(null);
		return this.edit(course);
	}

	@Override
	public Result edit(Courses course) {
		if (course.getId() == null) {
			return Result.emptyError("id");
		}

		Result result = checkFields(course, false);
		if (result != null) {
			return result;
		}

		result = new Result();
		if (!super.lambdaQuery().eq(Courses::getId, course.getId()).exists()) {
			result.setMsg("课程不存在!");
			return result;
		}

		course.setAdd_time(null);
		course.setAdd_user(null);
		result.setSuccess(super.updateById(course));
		if (result.isSuccess()) {
			result.setData(EntityUtil.ToMap(course));
		}
		return result;

	}
}
