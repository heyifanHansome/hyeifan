package com.stylefeng.guns.modular.activity.service;

import com.stylefeng.guns.modular.system.model.Activity;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动管理表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-21
 */
public interface IActivityService extends IService<Activity> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
