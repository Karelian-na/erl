package cn.karelian.erl.service;

import cn.karelian.erl.entity.Logs;
import cn.karelian.erl.mapper.LogsMapper;
import cn.karelian.erl.service.interfaces.ILogsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class LogsService extends ErlServiceImpl<LogsMapper, Logs> implements ILogsService {
}
