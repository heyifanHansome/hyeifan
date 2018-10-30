package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.Works;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 作品管理表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface WorksMapper extends BaseMapper<Works> {

    List<Map<String, Object>> list(@Param("condition") String condition);


    List<Works> findByCommdCretaeTime(@Param("createTime") Date createTime);

}
