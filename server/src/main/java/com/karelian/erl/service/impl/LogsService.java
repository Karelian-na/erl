package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Logs;
import com.karelian.erl.mapper.LogsMapper;
import com.karelian.erl.service.ILogsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class LogsService extends ServiceImpl<LogsMapper, Logs> implements ILogsService {
}
