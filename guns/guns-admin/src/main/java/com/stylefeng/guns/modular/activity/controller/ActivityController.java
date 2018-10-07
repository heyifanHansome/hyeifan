package com.stylefeng.guns.modular.activity.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroUser;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.system.model.ColumnType;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.system.warpper.ActivityWarpper;
import org.apache.shiro.SecurityUtils;
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
    private IColumnTypeService columnTypeService;

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
        User user = userService.selectById(activity.getUid());
        model.addAttribute("item", activity);
        model.addAttribute("userName",user.getName());
        if(activity.getSourceId()==0){

            model.addAttribute("source","官方");
        }else {

            model.addAttribute("source","个人");
        }

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
        return super.warpObject(new ActivityWarpper(list));
    }

    /**
     * 新增活动管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Activity activity) {
        EntityWrapper<ColumnType> columnTypeEntityWrapper = new EntityWrapper<>();
        columnTypeEntityWrapper.eq("name", "活动");
        List<ColumnType> columnTypes = columnTypeService.selectList(columnTypeEntityWrapper);
        activity.setColumnId(columnTypes.get(0).getId());
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Boolean checkRole = SecurityUtils.getSubject().hasRole("administrator");
        if (checkRole) {
            activity.setSourceId(0);
        } else {
            activity.setSourceId(1);
        }
        activity.setPublishIp("192.0.0.1");
        activity.setUid(shiroUser.getId());
        activity.setCreateTime(new DateTime());


        activityService.insert(activity);
        return SUCCESS_TIP;
    }

    /**
     * 删除活动管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer activityId) {
        activityService.deleteById(activityId);
        return SUCCESS_TIP;
    }

    /**
     * 修改活动管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Activity activity) {
        activity.setUpdateTime(new DateTime());
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
