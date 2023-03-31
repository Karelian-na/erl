package cn.karelian.erl.mapper;

import java.util.List;

public interface DeclarationItemMapper<T> extends ErlMapper<T> {
	public Boolean delete(List<Integer> ids);
}
