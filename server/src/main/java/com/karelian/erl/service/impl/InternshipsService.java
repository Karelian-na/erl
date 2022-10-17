package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Internships;
import com.karelian.erl.mapper.InternshipsMapper;
import com.karelian.erl.service.IInternshipsService;
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
public class InternshipsService extends ServiceImpl<InternshipsMapper, Internships> implements IInternshipsService {

}
