package cn.karelian.erl.service;

import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.ViewEditParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.entity.TableFieldsInfo;
import cn.karelian.erl.entity.ViewsInfo;
import cn.karelian.erl.errors.FieldError;
import cn.karelian.erl.mapper.ViewsInfoMapper;
import cn.karelian.erl.mapper.view.FieldsInfoViewMapper;
import cn.karelian.erl.mapper.view.ViewsMapper;
import cn.karelian.erl.service.interfaces.IDatabasesService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.PageData;
import cn.karelian.erl.utils.WebPageInfo;
import cn.karelian.erl.view.FieldsInfoView;
import cn.karelian.erl.view.Views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

/**
 * <p>
 * 学生参见本领域国内外重要学术会议表(研究生) 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-15
 */
@Service
public class DatabasesService extends ErlServiceImpl<ViewsInfoMapper, ViewsInfo> implements IDatabasesService {
	@Autowired
	private ViewsMapper viewsMapper;
	@Autowired
	private ViewsInfoMapper viewsInfoMapper;
	@Autowired
	private FieldsInfoViewMapper fieldsInfoViewMapper;
	@Autowired
	private TableFieldsInfoService tableFieldsInfoService;

	@Override
	public Result index(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		Result result = new Result();
		PageData<Views> pageData = new PageData<>();
		if (params.initPageSize == null) {
			pageData.data = viewsMapper.selectList(null);
			result.setData(pageData);
		} else {
			WebPageInfo<Views> info = super.getWebPageInfo(Views.class);
			info.pageData = pageData;
			info.pageData.data = viewsMapper.selectList(null);
			result.setData(info);
		}
		result.setSuccess(true);
		pageData.totalCount = Integer.valueOf(pageData.data.size()).longValue();
		return result;
	}

	@Override
	public Result editindex(String viewName) {
		int code = EntityUtil.CheckStringField(viewName, 1, 30, true);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("viewName", code);
		}

		Result result = new Result();

		LambdaQueryWrapper<Views> vlqw = new LambdaQueryWrapper<>();
		vlqw.eq(Views::getView_name, viewName);
		result.setSuccess(viewsMapper.exists(vlqw));

		if (!result.isSuccess()) {
			result.setMsg("请求视图不存在!");
			return result;
		}

		LambdaQueryWrapper<FieldsInfoView> lqw = new LambdaQueryWrapper<>();
		lqw.eq(FieldsInfoView::getTable_name, viewName);
		result.setData(fieldsInfoViewMapper.selectList(lqw));

		return result;
	}

	@Override
	@Transactional(rollbackFor = TransactionFailedException.class)
	public Result edit(ViewEditParam params) throws TransactionFailedException {
		int code = EntityUtil.CheckStringField(params.viewName, 1, 30, false);
		if (code != StatusCode.ERROR_SUCCESS) {
			return Result.fieldError("viewName", code);
		}

		Result result = new Result();
		LambdaQueryWrapper<Views> lqw = new LambdaQueryWrapper<>();
		lqw.eq(Views::getView_name, params.viewName);
		if (!viewsMapper.exists(lqw)) {
			result.setMsg("视图不存在!");
			return result;
		}

		if (params.fields != null) {
			for (String key : params.fields.keySet()) {
				TableFieldsInfo info = params.fields.get(key);
				code = EntityUtil.CheckStringField(info.getDisplay_name(), 2, 15, false);
				if (code != StatusCode.ERROR_SUCCESS) {
					return Result.fieldError("fields." + key + ".field_name", code);
				} else if (info.getEditable() != null && info.getEditable() && key.matches("((.*id)|.*(user|time))$")) {
					result.setMsg("不能修改字段" + key + "的可编辑性!");
					return result;
				}
			}

			LambdaUpdateWrapper<TableFieldsInfo> luw = new LambdaUpdateWrapper<>();
			for (String key : params.fields.keySet()) {
				TableFieldsInfo info = params.fields.get(key);
				luw.eq(TableFieldsInfo::getTable_name, params.viewName)
						.eq(TableFieldsInfo::getField_name, key);
				if (tableFieldsInfoService.getBaseMapper().exists(luw)) {
					if (!tableFieldsInfoService.update(info, luw)) {
						throw new TransactionFailedException(result);
					}
				} else {
					info.setTable_name(params.viewName);
					info.setField_name(key);
					if (!tableFieldsInfoService.save(info)) {
						throw new TransactionFailedException(result);
					}
				}
				luw.clear();
			}
		} else if (params.comment == null) {
			return Result.fieldError("fields | comment", FieldError.EMPTY);
		}

		ViewsInfo viewInfo = new ViewsInfo();
		if (params.comment != null) {
			if (params.comment.length() > 100) {
				return Result.fieldError("comment", FieldError.TOO_LONG);
			}
			viewInfo.setComment(params.comment);
		}

		viewInfo.setView_name(params.viewName);
		if (viewsInfoMapper.selectById(viewInfo) != null) {
			result.setSuccess(viewsInfoMapper.updateById(viewInfo) != 0);
		} else {
			result.setSuccess(viewsInfoMapper.insert(viewInfo) != 0);
		}
		if (!result.isSuccess()) {
			throw new TransactionFailedException(result);
		}

		result.setData(viewInfo);
		return result;
	}
}
