package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Applications;
import com.karelian.erl.mapper.ApplicationsMapper;
import com.karelian.erl.service.IApplicationsService;
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
public class ApplicationsService extends ServiceImpl<ApplicationsMapper, Applications> implements IApplicationsService {

}
