package com.stylefeng.guns.modular.infomation.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Information;
import com.stylefeng.guns.modular.infomation.service.IInformationService;

/**
 * 资讯管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-21 10:41:38
 */
@Controller
@RequestMapping("/information")
public class InformationController extends BaseController {

    private String PREFIX = "/infomation/information/";

    @Autowired
    private IInformationService informationService;

    /**
     * 跳转到资讯管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "information.html";
    }

    /**
     * 跳转到添加资讯管理
     */
    @RequestMapping("/information_add")
    public String informationAdd() {
        return PREFIX + "information_add.html";
    }

    /**
     * 跳转到修改资讯管理
     */
    @RequestMapping("/information_update/{informationId}")
    public String informationUpdate(@PathVariable Integer informationId, Model model) {
        Information information = informationService.selectById(informationId);
        model.addAttribute("item",information);
        LogObjectHolder.me().set(information);
        return PREFIX + "information_edit.html";
    }

    /**
     * 获取资讯管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return informationService.selectList(null);
    }

    /**
     * 新增资讯管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Information information) {
        informationService.insert(information);
        return SUCCESS_TIP;
    }

    /**
     * 删除资讯管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer informationId) {
        informationService.deleteById(informationId);
        return SUCCESS_TIP;
    }

    /**
     * 修改资讯管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Information information) {
        informationService.updateById(information);
        return SUCCESS_TIP;
    }

    /**
     * 资讯管理详情
     */
    @RequestMapping(value = "/detail/{informationId}")
    @ResponseBody
    public Object detail(@PathVariable("informationId") Integer informationId) {
        return informationService.selectById(informationId);
    }
}
