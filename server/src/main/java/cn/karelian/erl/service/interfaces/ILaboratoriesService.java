package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.DataTransferObject.LaboratoryReservationParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Laboratories;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
public interface ILaboratoriesService extends IService<Laboratories> {
	public Result resindex(IndexParam params) throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result reserve(LaboratoryReservationParam params);

	public Result add(Laboratories laboratory) throws NullRequestException;

	public Result edit(Laboratories laboratory);

	public Result delete(List<Integer> ids);
}
