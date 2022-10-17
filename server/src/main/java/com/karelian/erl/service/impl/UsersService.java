package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Users;
import com.karelian.erl.mapper.UsersMapper;
import com.karelian.erl.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理用户的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class UsersService extends ServiceImpl<UsersMapper, Users> implements IUsersService {
}
