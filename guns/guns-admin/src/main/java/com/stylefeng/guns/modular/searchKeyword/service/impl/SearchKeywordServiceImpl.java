package com.stylefeng.guns.modular.searchKeyword.service.impl;

import com.stylefeng.guns.modular.system.model.SearchKeyword;
import com.stylefeng.guns.modular.system.dao.SearchKeywordMapper;
import com.stylefeng.guns.modular.searchKeyword.service.ISearchKeywordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 关键词管理表,前台获取按照 order by orders desc,hit_num desc 排序 服务实现类
 * </p>
 *
 * @author 李俊
 * @since 2018-10-30
 */
@Service
public class SearchKeywordServiceImpl extends ServiceImpl<SearchKeywordMapper, SearchKeyword> implements ISearchKeywordService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
