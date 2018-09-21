package com.stylefeng.guns.modular.recruit.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Recruit;
import com.stylefeng.guns.modular.recruit.service.IRecruitService;

/**
 * 招聘管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 11:37:10
 */
@Controller
@RequestMapping("/recruit")
public class RecruitController extends BaseController {

    private String PREFIX = "/recruit/recruit/";

    @Autowired
    private IRecruitService recruitService;

    /**
     * 跳转到招聘管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "recruit.html";
    }

    /**
     * 跳转到添加招聘管理
     */
    @RequestMapping("/recruit_add")
    public String recruitAdd() {
        return PREFIX + "recruit_add.html";
    }

    /**
     * 跳转到修改招聘管理
     */
    @RequestMapping("/recruit_update/{recruitId}")
    public String recruitUpdate(@PathVariable Integer recruitId, Model model) {
        Recruit recruit = recruitService.selectById(recruitId);
        model.addAttribute("item",recruit);
        LogObjectHolder.me().set(recruit);
        return PREFIX + "recruit_edit.html";
    }

    /**
     * 获取招聘管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return recruitService.selectList(null);
    }

    /**
     * 新增招聘管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Recruit recruit) {
        recruitService.insert(recruit);
        return SUCCESS_TIP;
    }

    /**
     * 删除招聘管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer recruitId) {
        recruitService.deleteById(recruitId);
        return SUCCESS_TIP;
    }

    /**
     * 修改招聘管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Recruit recruit) {
        recruitService.updateById(recruit);
        return SUCCESS_TIP;
    }

    /**
     * 招聘管理详情
     */
    @RequestMapping(value = "/detail/{recruitId}")
    @ResponseBody
    public Object detail(@PathVariable("recruitId") Integer recruitId) {
        return recruitService.selectById(recruitId);
    }
}
