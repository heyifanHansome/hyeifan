package com.stylefeng.guns.modular.system.service;

import com.stylefeng.guns.modular.system.model.UserApi;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前台用户表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-10
 */
public interface IUserApiService extends IService<UserApi> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
