package com.stylefeng.guns.modular.userInfo.service;

import com.stylefeng.guns.modular.system.model.UserInfo;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户详情表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface IUserInfoService extends IService<UserInfo> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
