package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Courses;
import com.karelian.erl.mapper.CoursesMapper;
import com.karelian.erl.service.ICoursesService;
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
public class CoursesService extends ServiceImpl<CoursesMapper, Courses> implements ICoursesService {

}
