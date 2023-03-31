package cn.karelian.erl.service.interfaces;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.ViewEditParam;
import cn.karelian.erl.Exceptions.TransactionFailedException;
import cn.karelian.erl.entity.ViewsInfo;

/**
 * <p>
 * 学生参见本领域国内外重要学术会议表(研究生) 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-15
 */
public interface IDatabasesService extends IService<ViewsInfo> {

	public Result editindex(String viewName);

	public Result edit(ViewEditParam params) throws TransactionFailedException;
}
