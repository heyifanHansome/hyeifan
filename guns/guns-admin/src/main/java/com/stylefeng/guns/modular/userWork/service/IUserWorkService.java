package com.stylefeng.guns.modular.userWork.service;

import com.stylefeng.guns.modular.system.model.UserWork;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 作品关联表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface IUserWorkService extends IService<UserWork> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
