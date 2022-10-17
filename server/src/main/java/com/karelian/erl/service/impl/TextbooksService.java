package com.karelian.erl.service.impl;

import com.karelian.erl.entity.Textbooks;
import com.karelian.erl.mapper.TextbooksMapper;
import com.karelian.erl.service.ITextbooksService;
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
public class TextbooksService extends ServiceImpl<TextbooksMapper, Textbooks> implements ITextbooksService {

}
