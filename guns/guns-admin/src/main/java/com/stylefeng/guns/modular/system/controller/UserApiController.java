package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.service.IUserApiService;

import java.util.List;

/**
 * 控制器
 *
 * @author fengshuonan
 * @Date 2018-10-10 11:23:50
 */
@Controller
@RequestMapping("/userApi")
public class UserApiController extends BaseController {

    private String PREFIX = "/system/userApi/";

    @Autowired
    private IUserApiService userApiService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userApi.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/userApi_add")
    public String userApiAdd() {
        return PREFIX + "userApi_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/userApi_update/{userApiId}")
    public String userApiUpdate(@PathVariable Integer userApiId, Model model) {
        UserApi userApi = userApiService.selectById(userApiId);
        model.addAttribute("item", userApi);
        LogObjectHolder.me().set(userApi);
        return PREFIX + "userApi_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return userApiService.selectList(null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UserApi userApi) {
        userApiService.insert(userApi);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userApiId) {
        userApiService.deleteById(userApiId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UserApi userApi) {
        userApiService.updateById(userApi);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{userApiId}")
    @ResponseBody
    public Object detail(@PathVariable("userApiId") Integer userApiId) {
        return userApiService.selectById(userApiId);
    }


    /**
     * 详情
     */
    @RequestMapping(value = "/getAllUserApi")
    @ResponseBody
    public Object getAllUserApi() {
        List<UserApi> userApis = userApiService.selectList(null);
        return userApis;
    }


}
