package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.entity.Conferences;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 学生参见本领域国内外重要学术会议表(研究生) 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-15
 */
public interface IConferencesService extends IService<Conferences> {
	public Result delete(List<Integer> ids);
}
