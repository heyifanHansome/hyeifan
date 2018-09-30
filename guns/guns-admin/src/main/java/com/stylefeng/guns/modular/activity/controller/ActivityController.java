package com.stylefeng.guns.modular.activity.controller;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.city.service.ICityService;
import com.stylefeng.guns.modular.lijun.util.FinalStaticString;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.City;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.system.warpper.ActivityWarpper;
import com.stylefeng.guns.modular.system.warpper.UserInfoWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Activity;
import com.stylefeng.guns.modular.activity.service.IActivityService;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 活动管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-21 10:32:49
 */
@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseController {

    private String PREFIX = "/activity/activity/";

    @Autowired
    private IActivityService activityService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IUserService userService;
    /**
     * 跳转到活动管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "activity.html";
    }

    /**
     * 跳转到添加活动管理
     */
    @RequestMapping("/activity_add")
    public String activityAdd() {
        return PREFIX + "activity_add.html";
    }

    /**
     * 跳转到修改活动管理
     */
    @RequestMapping("/activity_update/{activityId}")
    public String activityUpdate(@PathVariable Integer activityId, Model model) {
        Activity activity = activityService.selectById(activityId);
        User user=new User();
        user.setId(Integer.valueOf(activity.getUid()));
        EntityWrapper<User>userEntityWrapper=new EntityWrapper<>();
        userEntityWrapper.setEntity(user);
        user=userService.selectOne(userEntityWrapper);
        switch (activity.getUid()){
            case "0":
                activity.setUid((user.getName()+"<span style='color:red;'>(管理员)</span>"));
                break;
            case "1":
                activity.setUid((user.getName()+"<span style='color:red;'>(用户)</span>"));
                break;
            default:
                activity.setUid((user.getName()+"<span style='color:red;'>(未知角色)</span>"));
                break;
        }
        model.addAttribute("item", activity);
        LogObjectHolder.me().set(activity);
        return PREFIX + "activity_edit.html";
    }

    /**
     * 获取活动管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = activityService.list(condition);
        for (Map<String, Object> activity : list) {
            City city=cityService.selectOne(new EntityWrapper<>(new City()).eq("id",activity.get("city_id")));
            activity.put("city_id",city!=null&&!Tool.isNull(city.getName())?city.getName():"<span style='color:red'>*所选城市不存在*</span>");
            if(!Tool.isNull(activity.get("source_id"))){
                switch (activity.get("source_id").toString()){
                    case "0":activity.put("source_id","官方");
                        break;
                    case "1":activity.put("source_id","个人");
                        break;
                    default:activity.put("source_id","其他");
                        break;
                }
            }else{
                activity.put("source_id","<span style='color:red'>*空*</span>");
            }
        }
        return list;
    }

    /**
     * 新增活动管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Activity activity,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
            ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET, old_object_name);
            ossClient.shutdown();
        }
        activity.setUid(String.valueOf(ShiroKit.getUser().getId()));
        activity.setPublishIp(Tool.getIpAdrress());
        activity.setCreateTime(new Date(System.currentTimeMillis()));
        activityService.insert(activity);
        return SUCCESS_TIP;
    }

    /**
     * 删除活动管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer activityId) {
        Activity activity = activityService.selectById(activityId);
        OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
        ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET, activity.getObject_name());
        ossClient.shutdown();
        activityService.deleteById(activityId);
        return SUCCESS_TIP;
    }

    /**
     * 修改活动管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Activity activity,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
            ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET, old_object_name);
            ossClient.shutdown();
        }
        activity.setUid(String.valueOf(ShiroKit.getUser().getId()));
        activity.setPublishIp(Tool.getIpAdrress());
        activity.setUpdateTime(new Date(System.currentTimeMillis()));
        activityService.updateById(activity);
        return SUCCESS_TIP;
    }

    /**
     * 活动管理详情
     */
    @RequestMapping(value = "/detail/{activityId}")
    @ResponseBody
    public Object detail(@PathVariable("activityId") Integer activityId) {
        return activityService.selectById(activityId);
    }
}
