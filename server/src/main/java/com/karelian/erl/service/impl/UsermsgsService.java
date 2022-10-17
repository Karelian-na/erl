package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Usermsgs;
import com.karelian.erl.mapper.UsermsgsMapper;
import com.karelian.erl.service.IUsermsgsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理用户基本信息的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-15
 */
@Service
public class UsermsgsService extends ServiceImpl<UsermsgsMapper, Usermsgs> implements IUsermsgsService {

}
