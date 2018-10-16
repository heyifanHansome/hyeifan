package com.stylefeng.guns.modular.systemSetting.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.modular.lijun.util.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.systemSetting.service.ISettingService;

import java.util.List;

/**
 * 系统设置控制器
 *
 * @author fengshuonan
 * @Date 2018-10-15 18:51:47
 */
@Controller
@RequestMapping("/setting")
public class SettingController extends BaseController {

    private String PREFIX = "/systemSetting/setting/";

    @Autowired
    private ISettingService settingService;

    /**
     * 跳转到系统设置首页
     */
    @RequestMapping("")
    public String index(Model model) {
        Setting setting=new Setting();
        setting.setId(1);
        List<Setting> settingList=settingService.selectList(new EntityWrapper<>(setting));
        model.addAttribute("item",!Tool.listIsNull(settingList)?settingList.get(0):setting);
        LogObjectHolder.me().set(setting);
        return PREFIX + "setting_edit.html";
    }


    /**
     * 修改系统设置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Setting setting) {
        Setting setting1=settingService.selectById(setting.getId());
        if(setting1==null){
            settingService.insertAllColumn(setting);
        }else{
            settingService.updateById(setting);
        }
        return SUCCESS_TIP;
    }

    /**
     * 系统设置详情
     */
    @RequestMapping(value = "/detail/{settingId}")
    @ResponseBody
    public Object detail(@PathVariable("settingId") Integer settingId) {
        return settingService.selectById(settingId);
    }
}
