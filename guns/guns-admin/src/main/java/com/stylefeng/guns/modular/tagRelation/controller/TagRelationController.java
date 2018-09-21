package com.stylefeng.guns.modular.tagRelation.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.TagRelation;
import com.stylefeng.guns.modular.tagRelation.service.ITagRelationService;

/**
 * 标签关联控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 16:31:06
 */
@Controller
@RequestMapping("/tagRelation")
public class TagRelationController extends BaseController {

    private String PREFIX = "/tagRelation/tagRelation/";

    @Autowired
    private ITagRelationService tagRelationService;

    /**
     * 跳转到标签关联首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "tagRelation.html";
    }

    /**
     * 跳转到添加标签关联
     */
    @RequestMapping("/tagRelation_add")
    public String tagRelationAdd() {
        return PREFIX + "tagRelation_add.html";
    }

    /**
     * 跳转到修改标签关联
     */
    @RequestMapping("/tagRelation_update/{tagRelationId}")
    public String tagRelationUpdate(@PathVariable Integer tagRelationId, Model model) {
        TagRelation tagRelation = tagRelationService.selectById(tagRelationId);
        model.addAttribute("item",tagRelation);
        LogObjectHolder.me().set(tagRelation);
        return PREFIX + "tagRelation_edit.html";
    }

    /**
     * 获取标签关联列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return tagRelationService.selectList(null);
    }

    /**
     * 新增标签关联
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(TagRelation tagRelation) {
        tagRelationService.insert(tagRelation);
        return SUCCESS_TIP;
    }

    /**
     * 删除标签关联
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer tagRelationId) {
        tagRelationService.deleteById(tagRelationId);
        return SUCCESS_TIP;
    }

    /**
     * 修改标签关联
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(TagRelation tagRelation) {
        tagRelationService.updateById(tagRelation);
        return SUCCESS_TIP;
    }

    /**
     * 标签关联详情
     */
    @RequestMapping(value = "/detail/{tagRelationId}")
    @ResponseBody
    public Object detail(@PathVariable("tagRelationId") Integer tagRelationId) {
        return tagRelationService.selectById(tagRelationId);
    }
}
