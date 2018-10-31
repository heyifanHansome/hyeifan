package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.SearchKeyword;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 关键词管理表,前台获取按照 order by orders desc,hit_num desc 排序 Mapper 接口
 * </p>
 *
 * @author 李俊
 * @since 2018-10-30
 */
public interface SearchKeywordMapper extends BaseMapper<SearchKeyword> {
    List<Map<String, Object>> list(@Param("condition") String condition);
}
