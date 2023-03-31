package cn.karelian.erl.service.interfaces;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.view.FieldsInfoView;
import cn.karelian.erl.utils.WebPageInfo;

public interface IErlService<T> extends IService<T> {
	public List<FieldsInfoView> getFields() throws IllegalAccessException;

	public List<FieldsInfoView> getFields(QueryWrapper<T> qw) throws IllegalAccessException;

	public WebPageInfo<T> getWebPageInfo()
			throws IllegalAccessException, NullRequestException,
			PermissionNotFoundException;

	public WebPageInfo<T> getWebPageInfo(QueryWrapper<T> qw)
			throws IllegalAccessException, NullRequestException,
			PermissionNotFoundException;

	public Result getPageData(IndexParam params, QueryWrapper<T> qw);

	public Result index(IndexParam params)
			throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result index(IndexParam params, QueryWrapper<T> qw)
			throws IllegalAccessException, NullRequestException,
			PermissionNotFoundException;
}