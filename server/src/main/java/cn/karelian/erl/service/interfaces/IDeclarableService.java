package cn.karelian.erl.service.interfaces;

import java.util.List;
import java.util.Map;

import cn.karelian.erl.Result;
import cn.karelian.erl.view.FieldsInfoView;

public interface IDeclarableService<T> extends IErlService<T> {
	public Map<String, Object> declareindex(List<FieldsInfoView> addFields) throws IllegalAccessException;

	public Result declare(T entity);
}
