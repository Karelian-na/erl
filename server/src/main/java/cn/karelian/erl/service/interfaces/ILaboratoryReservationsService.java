package cn.karelian.erl.service.interfaces;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.AuditParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.entity.LaboratoryReservations;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-02-15
 */
public interface ILaboratoryReservationsService extends IService<LaboratoryReservations> {
	public Result audit(AuditParam params) throws NullRequestException;
}
