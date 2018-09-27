package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.Classroom;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 星厨课堂 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface ClassroomMapper extends BaseMapper<Classroom> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
