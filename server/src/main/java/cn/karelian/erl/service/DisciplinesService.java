package cn.karelian.erl.service;

import cn.karelian.erl.entity.Disciplines;
import cn.karelian.erl.mapper.DisciplinesMapper;
import cn.karelian.erl.service.interfaces.IDisciplinesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-11-19
 */
@Service
public class DisciplinesService extends ErlServiceImpl<DisciplinesMapper, Disciplines>
		implements IDisciplinesService {

}
