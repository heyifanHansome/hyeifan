package com.stylefeng.guns.modular.searchKeyword.service;

import com.stylefeng.guns.modular.system.model.SearchKeyword;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 关键词管理表,前台获取按照 order by orders desc,hit_num desc 排序 服务类
 * </p>
 *
 * @author 李俊
 * @since 2018-10-30
 */
public interface ISearchKeywordService extends IService<SearchKeyword> {
    List<Map<String, Object>> list(@Param("condition") String condition);

}
