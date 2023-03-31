package cn.karelian.erl.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Result;
import cn.karelian.erl.StatusCode;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Permissions;
import cn.karelian.erl.mapper.ErlMapper;
import cn.karelian.erl.mapper.PermissionsMapper;
import cn.karelian.erl.mapper.view.FieldsInfoViewMapper;
import cn.karelian.erl.service.interfaces.IErlService;
import cn.karelian.erl.utils.EntityUtil;
import cn.karelian.erl.utils.OperButton;
import cn.karelian.erl.utils.PageData;
import cn.karelian.erl.utils.SpringContextUtil;
import cn.karelian.erl.utils.Utils;
import cn.karelian.erl.utils.WebPageInfo;
import cn.karelian.erl.view.FieldsInfoView;

public class ErlServiceImpl<M extends ErlMapper<T>, T> extends ServiceImpl<M, T> implements IErlService<T> {
	private static FieldsInfoViewMapper fieldsInfoViewMapper;
	private static PermissionsMapper permissionsMapper;

	public ErlMapper<?> getViewMapper() {
		return super.baseMapper;
	}

	private static void getBeans() {
		fieldsInfoViewMapper = SpringContextUtil.getBean(FieldsInfoViewMapper.class);
		permissionsMapper = SpringContextUtil.getBean(PermissionsMapper.class);
	}

	public static <K> List<FieldsInfoView> getFields(Class<K> clszz) throws IllegalAccessException {
		return ErlServiceImpl.getFields(clszz, null);
	}

	public static <K> List<FieldsInfoView> getFields(Class<K> clszz, QueryWrapper<K> qw) throws IllegalAccessException {
		if (fieldsInfoViewMapper == null) {
			getBeans();
		}

		String viewName = TableInfoHelper.getTableInfo(clszz).getTableName();
		LambdaQueryWrapper<FieldsInfoView> lqw = new LambdaQueryWrapper<>();
		lqw.eq(FieldsInfoView::getTable_name, viewName);
		if (qw != null && qw.getSqlSelect() != null) {
			lqw.in(FieldsInfoView::getField_name, List.of(qw.getSqlSelect().split(",")));
		}
		List<FieldsInfoView> fields = fieldsInfoViewMapper.selectList(lqw);
		if (fields.size() == 0) {
			throw new IllegalAccessException();
		}
		return fields;
	}

	public static <K> WebPageInfo<K> getWebPageInfo(Class<K> clszz)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return ErlServiceImpl.getWebPageInfo(clszz, null);
	}

	public static <K> WebPageInfo<K> getWebPageInfo(Class<K> clszz, QueryWrapper<K> qw)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		if (permissionsMapper == null) {
			getBeans();
		}
		HttpServletRequest request = Utils.getRequest();

		Long uid = (Long) request.getSession().getAttribute("id");
		String url = request.getRequestURI().replace("/index", "/");

		Permissions permission = permissionsMapper.getByUrl(url);
		if (null == permission) {
			permission = permissionsMapper.getByUrl(request.getRequestURI());
			if (null == permission) {
				throw new PermissionNotFoundException();
			}
		}

		WebPageInfo<K> info = new WebPageInfo<>();

		// 获取展示的字段
		info.fields = ErlServiceImpl.getFields(clszz, qw);

		// 获取操作权限
		info.operButtons = new ArrayList<>();
		for (Permissions permi : permissionsMapper.getAuthorizedOperationPermissions(uid, permission)) {
			info.operButtons.add(new OperButton(permi));
		}
		return info;

	}

	public static <K> Result getPageData(ErlMapper<K> mapper, IndexParam params, QueryWrapper<K> qw) {
		if (null == qw) {
			qw = new QueryWrapper<>();
		}

		if (params.one) {
			int code = EntityUtil.CheckStringField(params.searchKey, 5, 50, true);
			if (code != StatusCode.ERROR_SUCCESS) {
				return Result.fieldError("searchKey", code);
			}
			code = EntityUtil.CheckStringField(params.searchField, 4, 20, true);
			if (code != StatusCode.ERROR_SUCCESS) {
				return Result.fieldError("searchField", code);
			}

			qw.eq(params.searchField, params.searchKey);
			return new Result(true, mapper.selectOne(qw));
		} else {
			int code = EntityUtil.CheckNumberField(params.pageIdx, 0L, 1000L, true);
			if (code != StatusCode.ERROR_SUCCESS) {
				return Result.fieldError("pageIdx", code);
			}
			code = EntityUtil.CheckNumberField(params.pageSize, 20L, 200L, true);
			if (code != StatusCode.ERROR_SUCCESS) {
				return Result.fieldError("pageSize", code);
			}

			if (!ObjectUtils.isEmpty(params.searchKey)) {
				code = EntityUtil.CheckStringField(params.searchField, 4, 20, true);
				if (code != StatusCode.ERROR_SUCCESS) {
					return Result.fieldError("searchField", code);
				}
				qw.like(params.searchField, params.searchKey);
			}

			Page<K> page = new Page<>();
			page.setCurrent(params.pageIdx);
			page.setSize(params.pageSize);
			mapper.selectPage(page, qw);

			PageData<K> pageData = new PageData<>();
			pageData.totalCount = page.getTotal();
			pageData.curPageIdx = page.getCurrent();
			pageData.data = page.getRecords();

			Long pageAmount = Double.valueOf(Math.ceil(pageData.totalCount.doubleValue() / params.pageSize))
					.longValue();
			if (params.pageIdx > pageAmount) {
				params.pageIdx = pageAmount;
			}
			return new Result(true, pageData);
		}
	}

	public static <K> Result index(ErlMapper<K> mapper, IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return ErlServiceImpl.index(mapper, params, null);
	}

	@SuppressWarnings("unchecked")
	public static <K> Result index(ErlMapper<K> mapper, IndexParam params, QueryWrapper<K> qw)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		if (params.initPageSize != null) {
			if (params.initPageSize > 100 || params.initPageSize < 0) {
				return Result.InvalidArgument;
			}

			Class<?>[] clszzes = AopProxyUtils.proxiedUserInterfaces(mapper);
			Type[] type = clszzes[0].getGenericInterfaces();
			Class<K> clszz = (Class<K>) ((ParameterizedType) (type[0])).getActualTypeArguments()[0];

			WebPageInfo<K> info = ErlServiceImpl.getWebPageInfo(clszz, qw);

			params.pageIdx = 1L;
			params.pageSize = params.initPageSize;

			Result result = ErlServiceImpl.getPageData(mapper, params, qw);
			info.pageData = PageData.class.cast(result.getData());

			result.setData(info);
			return result;
		} else {
			return ErlServiceImpl.getPageData(mapper, params, qw);
		}
	}

	@Override
	public List<FieldsInfoView> getFields() throws IllegalAccessException {
		return ErlServiceImpl.getFields(super.entityClass, null);
	}

	@Override
	public List<FieldsInfoView> getFields(QueryWrapper<T> qw) throws IllegalAccessException {
		return ErlServiceImpl.getFields(super.entityClass, qw);
	}

	@Override
	public WebPageInfo<T> getWebPageInfo()
			throws IllegalAccessException, NullRequestException,
			PermissionNotFoundException {
		return ErlServiceImpl.getWebPageInfo(super.entityClass);
	}

	@Override
	public WebPageInfo<T> getWebPageInfo(QueryWrapper<T> qw)
			throws IllegalAccessException, NullRequestException,
			PermissionNotFoundException {
		return ErlServiceImpl.getWebPageInfo(super.entityClass, qw);
	}

	@Override
	public Result getPageData(IndexParam params, QueryWrapper<T> qw) {
		return ErlServiceImpl.getPageData(super.baseMapper, params, qw);
	}

	@Override
	public Result index(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return ErlServiceImpl.index(this.getViewMapper(), params);
	}

	@Override
	public Result index(IndexParam params, QueryWrapper<T> qw)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException {
		return ErlServiceImpl.index(super.baseMapper, params, qw);
	}
}
