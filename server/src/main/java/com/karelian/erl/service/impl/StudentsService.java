package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Students;
import com.karelian.erl.mapper.StudentsMapper;
import com.karelian.erl.service.IStudentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理学生信息的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class StudentsService extends ServiceImpl<StudentsMapper, Students> implements IStudentsService {

}
