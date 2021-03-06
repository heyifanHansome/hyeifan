package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.UserInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户详情表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
