package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.entity.Declarations;
import cn.karelian.erl.DataTransferObject.AuditParam;
import cn.karelian.erl.DataTransferObject.DeclareParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.TransactionFailedException;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理各种申报的表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-02-04
 */
public interface IDeclarationsService extends IService<Declarations> {
	public IErlService<?> getServiceByType(String type);

	public Result auditindex(String group, Integer refId);

	public Result audit(AuditParam params) throws NullRequestException;

	public Result declareindex(String type) throws IllegalAccessException;

	public Result declare(String value, DeclareParam params) throws TransactionFailedException;
}
