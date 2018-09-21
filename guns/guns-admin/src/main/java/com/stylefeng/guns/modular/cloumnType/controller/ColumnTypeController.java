package com.stylefeng.guns.modular.cloumnType.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.ColumnType;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;

/**
 * 栏目分类控制器
 *
 * @author fengshuonan
 * @Date 2018-09-21 10:25:39
 */
@Controller
@RequestMapping("/columnType")
public class ColumnTypeController extends BaseController {

    private String PREFIX = "/cloumnType/columnType/";

    @Autowired
    private IColumnTypeService columnTypeService;

    /**
     * 跳转到栏目分类首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "columnType.html";
    }

    /**
     * 跳转到添加栏目分类
     */
    @RequestMapping("/columnType_add")
    public String columnTypeAdd() {
        return PREFIX + "columnType_add.html";
    }

    /**
     * 跳转到修改栏目分类
     */
    @RequestMapping("/columnType_update/{columnTypeId}")
    public String columnTypeUpdate(@PathVariable Integer columnTypeId, Model model) {
        ColumnType columnType = columnTypeService.selectById(columnTypeId);
        model.addAttribute("item",columnType);
        LogObjectHolder.me().set(columnType);
        return PREFIX + "columnType_edit.html";
    }

    /**
     * 获取栏目分类列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return columnTypeService.selectList(null);
    }

    /**
     * 新增栏目分类
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ColumnType columnType) {
        columnTypeService.insert(columnType);
        return SUCCESS_TIP;
    }

    /**
     * 删除栏目分类
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer columnTypeId) {
        columnTypeService.deleteById(columnTypeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改栏目分类
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ColumnType columnType) {
        columnTypeService.updateById(columnType);
        return SUCCESS_TIP;
    }

    /**
     * 栏目分类详情
     */
    @RequestMapping(value = "/detail/{columnTypeId}")
    @ResponseBody
    public Object detail(@PathVariable("columnTypeId") Integer columnTypeId) {
        return columnTypeService.selectById(columnTypeId);
    }
}
