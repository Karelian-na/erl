package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Roles;
import com.karelian.erl.mapper.RolesMapper;
import com.karelian.erl.service.IRolesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理角色的表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@Service
public class RolesService extends ServiceImpl<RolesMapper, Roles> implements IRolesService {

}
