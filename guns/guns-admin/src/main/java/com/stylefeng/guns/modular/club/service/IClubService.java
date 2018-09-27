package com.stylefeng.guns.modular.club.service;

import com.stylefeng.guns.modular.system.model.Club;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 星厨俱乐部 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
public interface IClubService extends IService<Club> {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
