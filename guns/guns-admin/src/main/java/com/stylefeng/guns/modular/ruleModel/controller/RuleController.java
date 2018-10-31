package com.stylefeng.guns.modular.ruleModel.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Rule;
import com.stylefeng.guns.modular.ruleModel.service.IRuleService;

/**
 * 收徒详情控制器
 *
 * @author fengshuonan
 * @Date 2018-10-30 17:41:19
 */
@Controller
@RequestMapping("/rule")
public class RuleController extends BaseController {

    private String PREFIX = "/ruleModel/rule/";

    @Autowired
    private IRuleService ruleService;

    /**
     * 跳转到收徒详情首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "rule.html";
    }

    /**
     * 跳转到添加收徒详情
     */
    @RequestMapping("/rule_add")
    public String ruleAdd() {
        return PREFIX + "rule_add.html";
    }

    /**
     * 跳转到修改收徒详情
     */
    @RequestMapping("/rule_update/{ruleId}")
    public String ruleUpdate(@PathVariable Integer ruleId, Model model) {
        Rule rule = ruleService.selectById(ruleId);
        model.addAttribute("item",rule);
        LogObjectHolder.me().set(rule);
        return PREFIX + "rule_edit.html";
    }

    /**
     * 获取收徒详情列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return ruleService.selectList(null);
    }

    /**
     * 新增收徒详情
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Rule rule) {
        ruleService.insert(rule);
        return SUCCESS_TIP;
    }

    /**
     * 删除收徒详情
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer ruleId) {
        ruleService.deleteById(ruleId);
        return SUCCESS_TIP;
    }

    /**
     * 修改收徒详情
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Rule rule) {
        ruleService.updateById(rule);
        return SUCCESS_TIP;
    }

    /**
     * 收徒详情详情
     */
    @RequestMapping(value = "/detail/{ruleId}")
    @ResponseBody
    public Object detail(@PathVariable("ruleId") Integer ruleId) {
        return ruleService.selectById(ruleId);
    }
}
