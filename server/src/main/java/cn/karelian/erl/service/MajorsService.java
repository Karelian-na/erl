package cn.karelian.erl.service;

import cn.karelian.erl.entity.Majors;
import cn.karelian.erl.mapper.MajorsMapper;
import cn.karelian.erl.service.interfaces.IMajorsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理专业的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class MajorsService extends ErlServiceImpl<MajorsMapper, Majors> implements IMajorsService {

}
