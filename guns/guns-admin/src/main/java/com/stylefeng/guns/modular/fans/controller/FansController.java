package com.stylefeng.guns.modular.fans.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Fans;
import com.stylefeng.guns.modular.fans.service.IFansService;

/**
 * 控制器
 *
 * @author fengshuonan
 * @Date 2018-10-29 14:32:34
 */
@Controller
@RequestMapping("/fans")
public class FansController extends BaseController {

    private String PREFIX = "/fans/fans/";

    @Autowired
    private IFansService fansService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "fans.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/fans_add")
    public String fansAdd() {
        return PREFIX + "fans_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/fans_update/{fansId}")
    public String fansUpdate(@PathVariable Integer fansId, Model model) {
        Fans fans = fansService.selectById(fansId);
        model.addAttribute("item",fans);
        LogObjectHolder.me().set(fans);
        return PREFIX + "fans_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return fansService.selectList(null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Fans fans) {
        fansService.insert(fans);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer fansId) {
        fansService.deleteById(fansId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Fans fans) {
        fansService.updateById(fans);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{fansId}")
    @ResponseBody
    public Object detail(@PathVariable("fansId") Integer fansId) {
        return fansService.selectById(fansId);
    }
}
