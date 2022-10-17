package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Teachers;
import com.karelian.erl.mapper.TeachersMapper;
import com.karelian.erl.service.ITeachersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理教师信息的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class TeachersService extends ServiceImpl<TeachersMapper, Teachers> implements ITeachersService {

}
