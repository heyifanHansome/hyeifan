package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.UserApi;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前台用户表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-10
 */
public interface UserApiMapper extends BaseMapper<UserApi> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
