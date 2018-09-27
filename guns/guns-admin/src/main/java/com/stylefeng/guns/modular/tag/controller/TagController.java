package com.stylefeng.guns.modular.tag.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.ColumnType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.tag.service.ITagService;

import java.util.Date;
import java.util.List;

/**
 * 标签管理表控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 16:25:24
 */
@Controller
@RequestMapping("/tag")
public class TagController extends BaseController {

    private String PREFIX = "/tag/tag/";

    @Autowired
    private ITagService tagService;
    @Autowired
    private IColumnTypeService columnTypeService;
    /**
     * 跳转到标签管理表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "tag.html";
    }

    /**
     * 跳转到添加标签管理表
     */
    @RequestMapping("/tag_add")
    public String tagAdd() {
        return PREFIX + "tag_add.html";
    }

    /**
     * 跳转到修改标签管理表
     */
    @RequestMapping("/tag_update/{tagId}")
    public String tagUpdate(@PathVariable Integer tagId, Model model) {
        Tag tag = tagService.selectById(tagId);
        model.addAttribute("item",tag);
        LogObjectHolder.me().set(tag);
        return PREFIX + "tag_edit.html";
    }

    /**
     * 获取标签管理表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper ew=new EntityWrapper(new Tag());
        if(!Tool.isNull(condition))ew.like("name",condition);
        ew.orderBy("create_time",false);
        List<Tag> tags=tagService.selectList(ew);
        for (Tag tag : tags) {
            if(Tool.isNull(tag.getColumnId())||tag.getColumnId().equals("0")){
                tag.setColumnId("<span style='color:red;'>通用标签</span>");
            }else{
                EntityWrapper ew_=new EntityWrapper(new ColumnType());
                ew_.eq("id",tag.getColumnId());
                List<ColumnType>columnTypeList=columnTypeService.selectList(ew_);
                tag.setColumnId(!Tool.listIsNull(columnTypeList)?columnTypeList.get(0).getName():"<span style='color:red;'>*对应栏目已被删除*</span>");
            }
        }
        return tags;
    }

    /**
     * 新增标签管理表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Tag tag) {
        tag.setCreateTime(new Date(System.currentTimeMillis()));
        tagService.insert(tag);
        return SUCCESS_TIP;
    }

    /**
     * 删除标签管理表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer tagId) {
        tagService.deleteById(tagId);
        return SUCCESS_TIP;
    }

    /**
     * 修改标签管理表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Tag tag) {
        tag.setUpdateTime(new Date(System.currentTimeMillis()));
        tagService.updateById(tag);
        return SUCCESS_TIP;
    }

    /**
     * 标签管理表详情
     */
    @RequestMapping(value = "/detail/{tagId}")
    @ResponseBody
    public Object detail(@PathVariable("tagId") Integer tagId) {
        return tagService.selectById(tagId);
    }
}
