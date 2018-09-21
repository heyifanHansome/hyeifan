package com.stylefeng.guns.modular.resumeLive.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.ResumeLive;
import com.stylefeng.guns.modular.resumeLive.service.IResumeLiveService;

/**
 * 用户资历控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 16:06:44
 */
@Controller
@RequestMapping("/resumeLive")
public class ResumeLiveController extends BaseController {

    private String PREFIX = "/resumeLive/resumeLive/";

    @Autowired
    private IResumeLiveService resumeLiveService;

    /**
     * 跳转到用户资历首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "resumeLive.html";
    }

    /**
     * 跳转到添加用户资历
     */
    @RequestMapping("/resumeLive_add")
    public String resumeLiveAdd() {
        return PREFIX + "resumeLive_add.html";
    }

    /**
     * 跳转到修改用户资历
     */
    @RequestMapping("/resumeLive_update/{resumeLiveId}")
    public String resumeLiveUpdate(@PathVariable Integer resumeLiveId, Model model) {
        ResumeLive resumeLive = resumeLiveService.selectById(resumeLiveId);
        model.addAttribute("item",resumeLive);
        LogObjectHolder.me().set(resumeLive);
        return PREFIX + "resumeLive_edit.html";
    }

    /**
     * 获取用户资历列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return resumeLiveService.selectList(null);
    }

    /**
     * 新增用户资历
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ResumeLive resumeLive) {
        resumeLiveService.insert(resumeLive);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户资历
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer resumeLiveId) {
        resumeLiveService.deleteById(resumeLiveId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户资历
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ResumeLive resumeLive) {
        resumeLiveService.updateById(resumeLive);
        return SUCCESS_TIP;
    }

    /**
     * 用户资历详情
     */
    @RequestMapping(value = "/detail/{resumeLiveId}")
    @ResponseBody
    public Object detail(@PathVariable("resumeLiveId") Integer resumeLiveId) {
        return resumeLiveService.selectById(resumeLiveId);
    }
}
