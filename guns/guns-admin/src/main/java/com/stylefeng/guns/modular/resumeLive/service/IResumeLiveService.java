package com.stylefeng.guns.modular.resumeLive.service;

import com.stylefeng.guns.modular.system.model.ResumeLive;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户经历管理表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface IResumeLiveService extends IService<ResumeLive> {

     List<Map<String, Object>> list(@Param("condition") String condition) ;
}
