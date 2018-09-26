package com.stylefeng.guns.modular.cloumnType.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.dao.Dao;
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

import java.sql.Date;
import java.sql.Wrapper;
import java.util.List;

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
    private Dao dao;

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
        EntityWrapper ew=new EntityWrapper(new ColumnType());
        if(!Tool.isNull(condition))ew.like("name",condition);
        ew.orderBy("orders",false);
        List<ColumnType>columnTypes=columnTypeService.selectList(ew);
        for (ColumnType type : columnTypes) {
            if(Tool.isNull(type.getParentId())||type.getParentId().equals("0")){
                type.setParentId("<span style='color:red;'>顶级目录</span>");
            }else{
                EntityWrapper ew_=new EntityWrapper(new ColumnType());
                ew_.eq("id",type.getParentId());
                List<ColumnType>columnTypeList=columnTypeService.selectList(ew_);
                type.setParentId(!Tool.listIsNull(columnTypeList)?columnTypeList.get(0).getName():"<span style='color:red;'>*上级分类已被删除*</span>");
            }
        }
        return columnTypes;
    }

    /**
     * 新增栏目分类
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ColumnType columnType) {
        columnType.setCreateTime(new Date(System.currentTimeMillis()));
        if(Tool.isNull(columnType.getParentId()))columnType.setParentId("0");
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
        columnType.setUpdateTime(new Date(System.currentTimeMillis()));
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


    @RequestMapping("/getColumnTypeList")
    @ResponseBody
    public Object getColumnTypeList(Integer id){
        return dao.selectBySQL("select * from sys_column_type where parent_id=0"+(!Tool.isNull(id)?" and id<>"+id:""));
    }
}
