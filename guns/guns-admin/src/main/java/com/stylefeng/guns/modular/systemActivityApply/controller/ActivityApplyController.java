package com.stylefeng.guns.modular.systemActivityApply.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.modular.activity.service.IActivityService;
import com.stylefeng.guns.modular.lijun.util.JavaBeanUtil;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.Activity;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.ActivityApply;
import com.stylefeng.guns.modular.systemActivityApply.service.IActivityApplyService;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 活动报名管理控制器
 *
 * @author fengshuonan
 * @Date 2018-10-16 17:32:33
 */
@Controller
@RequestMapping("/activityApply")
public class ActivityApplyController extends BaseController {

    private String PREFIX = "/systemActivityApply/activityApply/";
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IActivityService activityService;
    @Autowired
    private IActivityApplyService activityApplyService;

    /**
     * 跳转到活动报名管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "activityApply.html";
    }

    /**
     * 跳转到添加活动报名管理
     */
    @RequestMapping("/activityApply_add")
    public String activityApplyAdd() {
        return PREFIX + "activityApply_add.html";
    }

    /**
     * 跳转到修改活动报名管理
     */
    @RequestMapping("/activityApply_update/{activityApplyId}")
    public String activityApplyUpdate(@PathVariable Integer activityApplyId, Model model) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        ActivityApply activityApply = activityApplyService.selectById(activityApplyId);
        model.addAttribute("item",activityApply);
        LogObjectHolder.me().set(activityApply);
        return PREFIX + "activityApply_edit.html";
    }

    /**
     * 获取活动报名管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<ActivityApply>activityApplies=activityApplyService.selectList(null);
        for (ActivityApply activityApply : activityApplies) {
            activityApply.setUserApiId(!Tool.isNull(activityApply.getUserApiId())?String.valueOf(activityApply.getUserApiId().split(",").length):"0");
            EntityWrapper<Activity>entityWrapper=new EntityWrapper<>();
            entityWrapper.eq("id",!Tool.isNull(activityApply.getActivityId())?activityApply.getActivityId():0);
            Activity activity=activityService.selectOne(entityWrapper);
            activityApply.setActivityId(activity!=null?activity.getTitle():"<span style='color:red;'>活动已被删除</span>");
        }
        return activityApplies;
    }

    /**
     * 新增活动报名管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ActivityApply activityApply) {
        if(activityApplyService.selectCount(new EntityWrapper<>(new ActivityApply()).eq("activity_id",activityApply.getActivityId()))>0)return new ErrorTip(500,"添加失败,您选活动已经有报名表存在");
        activityApply.setCreateTime(new Date());
        activityApplyService.insert(activityApply);
        return SUCCESS_TIP;
    }

    /**
     * 删除活动报名管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer activityApplyId) {
        activityApplyService.deleteById(activityApplyId);
        return SUCCESS_TIP;
    }

    /**
     * 修改活动报名管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ActivityApply activityApply) {
        if(activityApplyService.selectCount(new EntityWrapper<>(new ActivityApply()).eq("activity_id",activityApply.getActivityId()).ne("id",activityApply.getActivityId()))>0)return new ErrorTip(500,"修改失败,您选活动已经有报名表存在");
        activityApply.setUpdateTime(new Date());
        activityApplyService.updateById(activityApply);
        return SUCCESS_TIP;
    }

    /**
     * 活动报名管理详情
     */
    @RequestMapping(value = "/detail/{activityApplyId}")
    @ResponseBody
    public Object detail(@PathVariable("activityApplyId") Integer activityApplyId) {
        return activityApplyService.selectById(activityApplyId);
    }

    @RequestMapping("activityList")
    @ResponseBody
    public Object activityList(){
        return activityService.list(null);
    }

    @RequestMapping("userList")
    @ResponseBody
    public Object usersByUserApiIds(){
        return userApiService.list(null);
    }
}
