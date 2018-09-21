package com.stylefeng.guns.modular.standard.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.EnlighteningStandard;
import com.stylefeng.guns.modular.standard.service.IEnlighteningStandardService;

/**
 * 厨师收徒标准控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 11:18:45
 */
@Controller
@RequestMapping("/enlighteningStandard")
public class EnlighteningStandardController extends BaseController {

    private String PREFIX = "/standard/enlighteningStandard/";

    @Autowired
    private IEnlighteningStandardService enlighteningStandardService;

    /**
     * 跳转到厨师收徒标准首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "enlighteningStandard.html";
    }

    /**
     * 跳转到添加厨师收徒标准
     */
    @RequestMapping("/enlighteningStandard_add")
    public String enlighteningStandardAdd() {
        return PREFIX + "enlighteningStandard_add.html";
    }

    /**
     * 跳转到修改厨师收徒标准
     */
    @RequestMapping("/enlighteningStandard_update/{enlighteningStandardId}")
    public String enlighteningStandardUpdate(@PathVariable Integer enlighteningStandardId, Model model) {
        EnlighteningStandard enlighteningStandard = enlighteningStandardService.selectById(enlighteningStandardId);
        model.addAttribute("item",enlighteningStandard);
        LogObjectHolder.me().set(enlighteningStandard);
        return PREFIX + "enlighteningStandard_edit.html";
    }

    /**
     * 获取厨师收徒标准列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return enlighteningStandardService.selectList(null);
    }

    /**
     * 新增厨师收徒标准
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(EnlighteningStandard enlighteningStandard) {
        enlighteningStandardService.insert(enlighteningStandard);
        return SUCCESS_TIP;
    }

    /**
     * 删除厨师收徒标准
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer enlighteningStandardId) {
        enlighteningStandardService.deleteById(enlighteningStandardId);
        return SUCCESS_TIP;
    }

    /**
     * 修改厨师收徒标准
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(EnlighteningStandard enlighteningStandard) {
        enlighteningStandardService.updateById(enlighteningStandard);
        return SUCCESS_TIP;
    }

    /**
     * 厨师收徒标准详情
     */
    @RequestMapping(value = "/detail/{enlighteningStandardId}")
    @ResponseBody
    public Object detail(@PathVariable("enlighteningStandardId") Integer enlighteningStandardId) {
        return enlighteningStandardService.selectById(enlighteningStandardId);
    }
}
