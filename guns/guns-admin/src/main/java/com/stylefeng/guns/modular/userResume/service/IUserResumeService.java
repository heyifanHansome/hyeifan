package com.stylefeng.guns.modular.userResume.service;

import com.stylefeng.guns.modular.system.model.UserResume;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户简历管理表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface IUserResumeService extends IService<UserResume> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
