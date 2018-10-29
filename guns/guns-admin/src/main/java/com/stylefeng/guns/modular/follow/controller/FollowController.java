package com.stylefeng.guns.modular.follow.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Follow;
import com.stylefeng.guns.modular.follow.service.IFollowService;

/**
 * 控制器
 *
 * @author fengshuonan
 * @Date 2018-10-29 14:32:45
 */
@Controller
@RequestMapping("/follow")
public class FollowController extends BaseController {

    private String PREFIX = "/follow/follow/";

    @Autowired
    private IFollowService followService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "follow.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/follow_add")
    public String followAdd() {
        return PREFIX + "follow_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/follow_update/{followId}")
    public String followUpdate(@PathVariable Integer followId, Model model) {
        Follow follow = followService.selectById(followId);
        model.addAttribute("item",follow);
        LogObjectHolder.me().set(follow);
        return PREFIX + "follow_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return followService.selectList(null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Follow follow) {
        followService.insert(follow);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer followId) {
        followService.deleteById(followId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Follow follow) {
        followService.updateById(follow);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{followId}")
    @ResponseBody
    public Object detail(@PathVariable("followId") Integer followId) {
        return followService.selectById(followId);
    }
}
