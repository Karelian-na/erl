package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.entity.Projects;
import cn.karelian.erl.utils.ProjectType;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-15
 */
public interface IProjectsService extends IService<Projects> {
	public Result delete(ProjectType type, List<Integer> ids);
}
