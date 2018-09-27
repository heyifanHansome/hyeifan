package com.stylefeng.guns.modular.classroom.service;

import com.stylefeng.guns.modular.system.model.Classroom;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 星厨课堂 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface IClassroomService extends IService<Classroom> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
