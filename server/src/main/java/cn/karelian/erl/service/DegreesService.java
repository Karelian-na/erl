package cn.karelian.erl.service;

import cn.karelian.erl.entity.Degrees;
import cn.karelian.erl.mapper.DegreesMapper;
import cn.karelian.erl.service.interfaces.IDegreesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-14
 */
@Service
public class DegreesService extends ErlServiceImpl<DegreesMapper, Degrees> implements IDegreesService {

}
