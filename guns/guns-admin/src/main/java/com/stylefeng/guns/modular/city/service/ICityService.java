package com.stylefeng.guns.modular.city.service;

import com.stylefeng.guns.modular.system.model.City;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 城市管理表city 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface ICityService extends IService<City> {
    List<Map<String, Object>> list(@Param("condition") String condition);
}
