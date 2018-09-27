package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.Information;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
资讯管理表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-21
 */
public interface InformationMapper extends BaseMapper<Information> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
