package com.stylefeng.guns.modular.works.service;

import com.stylefeng.guns.modular.system.model.Works;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 作品管理表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface IWorksService extends IService<Works> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
