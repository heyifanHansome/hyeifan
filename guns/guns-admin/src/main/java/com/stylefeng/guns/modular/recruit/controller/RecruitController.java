package com.stylefeng.guns.modular.recruit.controller;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.city.service.ICityService;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.lijun.util.SettingConfiguration;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.recruit.service.IRecruitService;

import java.sql.Date;
import java.util.List;

/**
 * 招聘管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 11:37:10
 */
@Controller
@RequestMapping("/recruit")
public class RecruitController extends BaseController {

    private String PREFIX = "/recruit/recruit/";
@Autowired
private SettingConfiguration settingConfiguration;
    @Autowired
    private IRecruitService recruitService;
    @Autowired
    private Dao dao;

    @Autowired
    private IColumnTypeService columnTypeService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IUserService userService;
    /**
     * 跳转到招聘管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "recruit.html";
    }

    /**
     * 跳转到添加招聘管理
     */
    @RequestMapping("/recruit_add")
    public String recruitAdd() {
        return PREFIX + "recruit_add.html";
    }

    /**
     * 跳转到修改招聘管理
     */
    @RequestMapping("/recruit_update/{recruitId}")
    public String recruitUpdate(@PathVariable Integer recruitId, Model model) {
        Recruit recruit = recruitService.selectById(recruitId);
        User user=new User();
        user.setId(Integer.valueOf(recruit.getUid()));
        EntityWrapper<User>userEntityWrapper=new EntityWrapper<>();
        userEntityWrapper.setEntity(user);
        user=userService.selectOne(userEntityWrapper);
        switch (recruit.getUid()){
            case "0":
                recruit.setUid((user.getName()+"<span style='color:red;'>(管理员)</span>"));
                break;
            case "1":
                recruit.setUid((user.getName()+"<span style='color:red;'>(用户)</span>"));
                break;
            default:
                recruit.setUid((user.getName()+"<span style='color:red;'>(未知角色)</span>"));
                break;
        }
        model.addAttribute("item",recruit);
        LogObjectHolder.me().set(recruit);
        return PREFIX + "recruit_edit.html";
    }

    /**
     * 获取招聘管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper ew=new EntityWrapper(new Recruit());
        if(!Tool.isNull(condition))ew.like("name",condition);
        List<Recruit> recruits=recruitService.selectList(ew);
        for (Recruit recruit : recruits) {
            EntityWrapper ew_=new EntityWrapper(new ColumnType());
            ew_.eq("id",recruit.getColumnId());
            List<ColumnType>columnTypeList=columnTypeService.selectList(ew_);
            recruit.setColumnId(!Tool.listIsNull(columnTypeList)?columnTypeList.get(0).getName():"<span style='color:red;'>*对应栏目已被删除*</span>");

            EntityWrapper ew2=new EntityWrapper(new City());
            ew2.eq("id",recruit.getCityId());
            List<City>cityList=cityService.selectList(ew2);
            recruit.setCityId(!Tool.listIsNull(cityList)?cityList.get(0).getName():"<span style='color:red;'>*对应城市已被删除*</span>");

            recruit.setSourceId(Tool.isNull(recruit.getSourceId())?"<span style='color:red;'>*未知来源*</span>":recruit.getSourceId().equals("0")?"官方":recruit.getSourceId().equals("1")?"个人":"<span style='color:red;'>*未知来源*</span>");
        }
        return recruits;
    }

    /**
     * 新增招聘管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Recruit recruit,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            Setting setting=settingConfiguration.getSetting();
            OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
            ossClient.deleteObject(setting.getAliOssBucket(), old_object_name);
            ossClient.shutdown();
        }
        recruit.setUid(String.valueOf(ShiroKit.getUser().getId()));
        recruit.setPublishIp(Tool.getIpAdrress());
        recruit.setCreateTime(new Date(System.currentTimeMillis()));
        recruitService.insert(recruit);
        return SUCCESS_TIP;
    }

    /**
     * 删除招聘管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer recruitId) {
        Recruit recruit = recruitService.selectById(recruitId);
        Setting setting=settingConfiguration.getSetting();
        OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
        ossClient.deleteObject(setting.getAliOssBucket(), recruit.getObject_name());
        ossClient.shutdown();
        recruitService.deleteById(recruitId);
        return SUCCESS_TIP;
    }

    /**
     * 修改招聘管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Recruit recruit,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            Setting setting=settingConfiguration.getSetting();
            OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
            ossClient.deleteObject(setting.getAliOssBucket(), old_object_name);
            ossClient.shutdown();
        }
        recruit.setUid(String.valueOf(ShiroKit.getUser().getId()));
        recruit.setPublishIp(Tool.getIpAdrress());
        recruit.setUpdatedTime(new Date(System.currentTimeMillis()));
        recruitService.updateById(recruit);
        return SUCCESS_TIP;
    }

    /**
     * 招聘管理详情
     */
    @RequestMapping(value = "/detail/{recruitId}")
    @ResponseBody
    public Object detail(@PathVariable("recruitId") Integer recruitId) {
        return recruitService.selectById(recruitId);
    }
}
