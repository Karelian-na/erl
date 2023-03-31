package cn.karelian.erl.service.interfaces;

import cn.karelian.erl.Result;
import cn.karelian.erl.DataTransferObject.IndexParam;
import cn.karelian.erl.Exceptions.NullRequestException;
import cn.karelian.erl.Exceptions.PermissionNotFoundException;
import cn.karelian.erl.entity.Maters;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-11-17
 */
public interface IMatersService extends IService<Maters> {
	public Result index(String group, String category, IndexParam params) throws IllegalAccessException, NullRequestException, PermissionNotFoundException;

	public Result download(String group, String category, List<Integer> ids);

	public Result upload(String group, String category, MultipartFile file);

	public Result delete(List<Integer> ids);
}
