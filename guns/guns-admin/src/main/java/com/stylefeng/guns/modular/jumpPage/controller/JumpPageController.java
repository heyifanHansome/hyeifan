package com.stylefeng.guns.modular.jumpPage.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.JumpPage;
import com.stylefeng.guns.modular.jumpPage.service.IJumpPageService;

/**
 * 首页跳转模块控制器
 *
 * @author fengshuonan
 * @Date 2018-10-23 09:54:46
 */
@Controller
@RequestMapping("/jumpPage")
public class JumpPageController extends BaseController {

    private String PREFIX = "/jumpPage/jumpPage/";

    @Autowired
    private IJumpPageService jumpPageService;

    /**
     * 跳转到首页跳转模块首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "jumpPage.html";
    }

    /**
     * 跳转到添加首页跳转模块
     */
    @RequestMapping("/jumpPage_add")
    public String jumpPageAdd() {
        return PREFIX + "jumpPage_add.html";
    }

    /**
     * 跳转到修改首页跳转模块
     */
    @RequestMapping("/jumpPage_update/{jumpPageId}")
    public String jumpPageUpdate(@PathVariable Integer jumpPageId, Model model) {
        JumpPage jumpPage = jumpPageService.selectById(jumpPageId);
        model.addAttribute("item",jumpPage);
        LogObjectHolder.me().set(jumpPage);
        return PREFIX + "jumpPage_edit.html";
    }

    /**
     * 获取首页跳转模块列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return jumpPageService.selectList(null);
    }

    /**
     * 新增首页跳转模块
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JumpPage jumpPage) {
        jumpPageService.insert(jumpPage);
        return SUCCESS_TIP;
    }

    /**
     * 删除首页跳转模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer jumpPageId) {
        jumpPageService.deleteById(jumpPageId);
        return SUCCESS_TIP;
    }

    /**
     * 修改首页跳转模块
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JumpPage jumpPage) {
        jumpPageService.updateById(jumpPage);
        return SUCCESS_TIP;
    }

    /**
     * 首页跳转模块详情
     */
    @RequestMapping(value = "/detail/{jumpPageId}")
    @ResponseBody
    public Object detail(@PathVariable("jumpPageId") Integer jumpPageId) {
        return jumpPageService.selectById(jumpPageId);
    }
}
