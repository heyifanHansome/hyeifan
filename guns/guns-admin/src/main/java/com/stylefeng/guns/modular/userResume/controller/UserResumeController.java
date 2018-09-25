package com.stylefeng.guns.modular.userResume.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.system.warpper.UserInfoWarpper;
import com.stylefeng.guns.modular.system.warpper.UserResumeWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.UserResume;
import com.stylefeng.guns.modular.userResume.service.IUserResumeService;

import java.util.List;
import java.util.Map;

/**
 * 用户简历管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 11:55:32
 */
@Controller
@RequestMapping("/userResume")
public class UserResumeController extends BaseController {

    private String PREFIX = "/userResume/userResume/";

    @Autowired
    private IUserResumeService userResumeService;

    /**
     * 跳转到用户简历管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userResume.html";
    }

    /**
     * 跳转到添加用户简历管理
     */
    @RequestMapping("/userResume_add")
    public String userResumeAdd() {
        return PREFIX + "userResume_add.html";
    }

    /**
     * 跳转到修改用户简历管理
     */
    @RequestMapping("/userResume_update/{userResumeId}")
    public String userResumeUpdate(@PathVariable Integer userResumeId, Model model) {
        UserResume userResume = userResumeService.selectById(userResumeId);
        model.addAttribute("item",userResume);
        LogObjectHolder.me().set(userResume);
        return PREFIX + "userResume_edit.html";
    }

    /**
     * 获取用户简历管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list  = userResumeService.list(condition);
        return super.warpObject(new UserResumeWarpper(list));
    }

    /**
     * 新增用户简历管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UserResume userResume) {
        userResume.setCreateTime(new DateTime());
        userResumeService.insert(userResume);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户简历管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userResumeId) {
        userResumeService.deleteById(userResumeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户简历管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UserResume userResume) {
        userResume.setUpdateTime(new DateTime());
        userResumeService.updateById(userResume);
        return SUCCESS_TIP;
    }

    /**
     * 用户简历管理详情
     */
    @RequestMapping(value = "/detail/{userResumeId}")
    @ResponseBody
    public Object detail(@PathVariable("userResumeId") Integer userResumeId) {
        return userResumeService.selectById(userResumeId);
    }
}
