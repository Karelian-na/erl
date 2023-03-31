package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Papers;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理论文的表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
public interface IPapersService extends IService<Papers> {
	public Result delete(List<Integer> ids);

	public Result postindex(IndexParam params) throws IllegalAccessException, NullRequestException, PermissionNotFoundException;
}
