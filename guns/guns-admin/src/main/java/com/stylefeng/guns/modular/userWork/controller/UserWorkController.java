package com.stylefeng.guns.modular.userWork.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.UserWork;
import com.stylefeng.guns.modular.userWork.service.IUserWorkService;

/**
 * 用户作品控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 15:31:52
 */
@Controller
@RequestMapping("/userWork")
public class UserWorkController extends BaseController {

    private String PREFIX = "/userWork/userWork/";

    @Autowired
    private IUserWorkService userWorkService;

    /**
     * 跳转到用户作品首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userWork.html";
    }

    /**
     * 跳转到添加用户作品
     */
    @RequestMapping("/userWork_add")
    public String userWorkAdd() {
        return PREFIX + "userWork_add.html";
    }

    /**
     * 跳转到修改用户作品
     */
    @RequestMapping("/userWork_update/{userWorkId}")
    public String userWorkUpdate(@PathVariable Integer userWorkId, Model model) {
        UserWork userWork = userWorkService.selectById(userWorkId);
        model.addAttribute("item",userWork);
        LogObjectHolder.me().set(userWork);
        return PREFIX + "userWork_edit.html";
    }

    /**
     * 获取用户作品列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return userWorkService.selectList(null);
    }

    /**
     * 新增用户作品
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UserWork userWork) {
        userWorkService.insert(userWork);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户作品
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userWorkId) {
        userWorkService.deleteById(userWorkId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户作品
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UserWork userWork) {
        userWorkService.updateById(userWork);
        return SUCCESS_TIP;
    }

    /**
     * 用户作品详情
     */
    @RequestMapping(value = "/detail/{userWorkId}")
    @ResponseBody
    public Object detail(@PathVariable("userWorkId") Integer userWorkId) {
        return userWorkService.selectById(userWorkId);
    }
}
