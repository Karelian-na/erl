package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.entity.Patents;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
public interface IPatentsService extends IService<Patents> {
	public Result delete(List<Integer> ids);
}
