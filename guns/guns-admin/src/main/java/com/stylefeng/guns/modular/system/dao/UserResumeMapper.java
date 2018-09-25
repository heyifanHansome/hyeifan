package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.UserResume;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户简历管理表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface UserResumeMapper extends BaseMapper<UserResume> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
