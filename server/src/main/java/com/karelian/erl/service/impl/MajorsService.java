package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Majors;
import com.karelian.erl.mapper.MajorsMapper;
import com.karelian.erl.service.IMajorsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class MajorsService extends ServiceImpl<MajorsMapper, Majors> implements IMajorsService {

}
