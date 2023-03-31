/*
 * @Author: Karelian_na
 */
package cn.karelian.erl.service;

import cn.karelian.erl.entity.RolePermAssoc;
import cn.karelian.erl.mapper.RolePermAssocMapper;
import cn.karelian.erl.service.interfaces.IRolePermAssocService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-10-16
 */
@Service
public class RolePermAssocService extends ServiceImpl<RolePermAssocMapper, RolePermAssoc> implements IRolePermAssocService {


	@Override
	public boolean isAuthorized(Integer pid, Byte rid) {
		QueryWrapper<RolePermAssoc> qw = new QueryWrapper<RolePermAssoc>();
		qw.eq("rid", rid);
		qw.eq("pid", pid);
		Long count = this.baseMapper.selectCount(qw);
		
		return count == 1;
	}
}
