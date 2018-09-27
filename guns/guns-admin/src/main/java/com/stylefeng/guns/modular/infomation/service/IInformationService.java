package com.stylefeng.guns.modular.infomation.service;

import com.stylefeng.guns.modular.system.model.Information;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
资讯管理表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-21
 */
public interface IInformationService extends IService<Information> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
