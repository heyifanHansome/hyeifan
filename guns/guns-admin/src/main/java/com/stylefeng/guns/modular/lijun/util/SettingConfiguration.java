package com.stylefeng.guns.modular.lijun.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.systemSetting.service.ISettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettingConfiguration {
    @Autowired
    private ISettingService settingService;
    @Bean
    public Setting getSetting(){
        Setting setting=new Setting();
        setting.setId(1);
        setting=settingService.selectOne(new EntityWrapper<>(setting));
        System.err.println("------------------");
        System.err.println("---加载系统配置---");
        System.err.println("------------------");
        return setting;
    }
}
