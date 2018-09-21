package com.stylefeng.guns.modular.activity.service.impl;

import com.stylefeng.guns.modular.system.model.Activity;
import com.stylefeng.guns.modular.system.dao.ActivityMapper;
import com.stylefeng.guns.modular.activity.service.IActivityService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活动管理表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-21
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

}
