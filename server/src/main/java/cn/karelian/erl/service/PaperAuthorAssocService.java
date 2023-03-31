package cn.karelian.erl.service;

import cn.karelian.erl.entity.PaperAuthorAssoc;
import cn.karelian.erl.mapper.PaperAuthorAssocMapper;
import cn.karelian.erl.service.interfaces.IPaperAuthorAssocService;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 论文作者关联表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-02-22
 */
@Service
public class PaperAuthorAssocService extends ErlServiceImpl<PaperAuthorAssocMapper, PaperAuthorAssoc>
		implements IPaperAuthorAssocService {

}
