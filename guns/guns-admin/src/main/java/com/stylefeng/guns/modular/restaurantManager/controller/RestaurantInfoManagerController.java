package com.stylefeng.guns.modular.restaurantManager.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.RestaurantInfoManager;
import com.stylefeng.guns.modular.restaurantManager.service.IRestaurantInfoManagerService;

/**
 * 餐厅信息管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 11:30:22
 */
@Controller
@RequestMapping("/restaurantInfoManager")
public class RestaurantInfoManagerController extends BaseController {

    private String PREFIX = "/restaurantManager/restaurantInfoManager/";

    @Autowired
    private IRestaurantInfoManagerService restaurantInfoManagerService;

    /**
     * 跳转到餐厅信息管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "restaurantInfoManager.html";
    }

    /**
     * 跳转到添加餐厅信息管理
     */
    @RequestMapping("/restaurantInfoManager_add")
    public String restaurantInfoManagerAdd() {
        return PREFIX + "restaurantInfoManager_add.html";
    }

    /**
     * 跳转到修改餐厅信息管理
     */
    @RequestMapping("/restaurantInfoManager_update/{restaurantInfoManagerId}")
    public String restaurantInfoManagerUpdate(@PathVariable Integer restaurantInfoManagerId, Model model) {
        RestaurantInfoManager restaurantInfoManager = restaurantInfoManagerService.selectById(restaurantInfoManagerId);
        model.addAttribute("item",restaurantInfoManager);
        LogObjectHolder.me().set(restaurantInfoManager);
        return PREFIX + "restaurantInfoManager_edit.html";
    }

    /**
     * 获取餐厅信息管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return restaurantInfoManagerService.selectList(null);
    }

    /**
     * 新增餐厅信息管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(RestaurantInfoManager restaurantInfoManager) {
        restaurantInfoManagerService.insert(restaurantInfoManager);
        return SUCCESS_TIP;
    }

    /**
     * 删除餐厅信息管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer restaurantInfoManagerId) {
        restaurantInfoManagerService.deleteById(restaurantInfoManagerId);
        return SUCCESS_TIP;
    }

    /**
     * 修改餐厅信息管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(RestaurantInfoManager restaurantInfoManager) {
        restaurantInfoManagerService.updateById(restaurantInfoManager);
        return SUCCESS_TIP;
    }

    /**
     * 餐厅信息管理详情
     */
    @RequestMapping(value = "/detail/{restaurantInfoManagerId}")
    @ResponseBody
    public Object detail(@PathVariable("restaurantInfoManagerId") Integer restaurantInfoManagerId) {
        return restaurantInfoManagerService.selectById(restaurantInfoManagerId);
    }
}
