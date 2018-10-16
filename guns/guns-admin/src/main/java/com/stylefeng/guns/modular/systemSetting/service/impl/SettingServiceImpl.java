package com.stylefeng.guns.modular.systemSetting.service.impl;

import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.dao.SettingMapper;
import com.stylefeng.guns.modular.systemSetting.service.ISettingService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统设置表 服务实现类
 * </p>
 *
 * @author lijun
 * @since 2018-10-15
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements ISettingService {

}
