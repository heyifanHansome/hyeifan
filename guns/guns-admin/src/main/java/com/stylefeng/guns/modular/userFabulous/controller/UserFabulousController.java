package com.stylefeng.guns.modular.userFabulous.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.UserFabulous;
import com.stylefeng.guns.modular.userFabulous.service.IUserFabulousService;

/**
 * 用户点赞控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 12:00:09
 */
@Controller
@RequestMapping("/userFabulous")
public class UserFabulousController extends BaseController {

    private String PREFIX = "/userFabulous/userFabulous/";

    @Autowired
    private IUserFabulousService userFabulousService;

    /**
     * 跳转到用户点赞首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userFabulous.html";
    }

    /**
     * 跳转到添加用户点赞
     */
    @RequestMapping("/userFabulous_add")
    public String userFabulousAdd() {
        return PREFIX + "userFabulous_add.html";
    }

    /**
     * 跳转到修改用户点赞
     */
    @RequestMapping("/userFabulous_update/{userFabulousId}")
    public String userFabulousUpdate(@PathVariable Integer userFabulousId, Model model) {
        UserFabulous userFabulous = userFabulousService.selectById(userFabulousId);
        model.addAttribute("item",userFabulous);
        LogObjectHolder.me().set(userFabulous);
        return PREFIX + "userFabulous_edit.html";
    }

    /**
     * 获取用户点赞列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return userFabulousService.selectList(null);
    }

    /**
     * 新增用户点赞
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UserFabulous userFabulous) {
        userFabulousService.insert(userFabulous);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户点赞
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userFabulousId) {
        userFabulousService.deleteById(userFabulousId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户点赞
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UserFabulous userFabulous) {
        userFabulousService.updateById(userFabulous);
        return SUCCESS_TIP;
    }

    /**
     * 用户点赞详情
     */
    @RequestMapping(value = "/detail/{userFabulousId}")
    @ResponseBody
    public Object detail(@PathVariable("userFabulousId") Integer userFabulousId) {
        return userFabulousService.selectById(userFabulousId);
    }
}
