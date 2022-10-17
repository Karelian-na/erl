package com.karelian.erl.controller;

import com.karelian.erl.ErlApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 管理角色的表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/roles")
public class RolesController {


    @PostMapping("/authorize")
	public Result authorize(Byte rid, @RequestBody HashMap<Integer, Byte> auths)
			throws Exception {
		if (super.canAccess()) {
			SqlSession sqlSession = null;
			try {
				sqlSession = ErlApplication.getSqlSession();
				sqlSession.getConnection().setAutoCommit(false);
				RolePermAssocMapper rolePermAssocMapper = sqlSession.getMapper(UserPermAssocMapper.class);

				for (Integer pid : auths.keySet()) {
					Byte auth = auths.get(pid);
					switch (auth) {
						case 0:
							rolePermAssocMapper.deleteByUnionKey(rid, pid);
							break;
						case 1:
							RolePermAssoc upa = new RolePermAssoc();
							upa.setRid(rid);
							upa.setPid(pid);
							rolePermAssocMapper.insertUnionKey(rid, pid);
							break;
						case 2:
						default:
							break;
					}
				}
				sqlSession.commit();
				return new Result(true);
			} catch (Exception e) {
				if (sqlSession != null) {
					sqlSession.rollback();
				}
				throw e;
			} finally {
				if (null != sqlSession) {
					sqlSession.close();
				}
			}
		}
		return Result.PermissionNotAllowed;
	}

}
