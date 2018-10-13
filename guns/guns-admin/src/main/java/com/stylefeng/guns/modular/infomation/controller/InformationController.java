package com.stylefeng.guns.modular.infomation.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.warpper.InformationWarpper;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.tagRelation.service.ITagRelationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.infomation.service.IInformationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 资讯管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-21 10:41:38
 */
@Controller
@RequestMapping("/information")
public class InformationController extends BaseController {

    private String PREFIX = "/infomation/information/";

    @Autowired
    private IInformationService informationService;

    @Autowired
    private IColumnTypeService columnTypeService;

    @Autowired
    private ITagService tagService;

    @Autowired
    private ITagRelationService tagRelationService;

    /**
     * 跳转到资讯管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "information.html";
    }

    /**
     * 跳转到添加资讯管理
     */
    @RequestMapping("/information_add")
    public String informationAdd() {
        return PREFIX + "information_add.html";
    }

    /**
     * 跳转到修改资讯管理
     */
    @RequestMapping("/information_update/{informationId}")
    public String informationUpdate(@PathVariable Integer informationId, Model model) {
        Information information = informationService.selectById(informationId);

        /**
         *回显标签定义
         */
        EntityWrapper<TagRelation> tagRelationEntityWrapper = new EntityWrapper<>();
        tagRelationEntityWrapper.eq("relation_id", information.getId()).and("common_type_id ={0}",information.getColumnId());
        List<TagRelation> tagRelations = tagRelationService.selectList(tagRelationEntityWrapper);
        List<Integer> multArr = new ArrayList<>();
        for (int i = 0; i < tagRelations.size(); i++) {
            Integer temp = tagRelations.get(i).getColumnId();
            multArr.add(temp);
        }
        model.addAttribute("multArr", multArr);


        ColumnType columnType = columnTypeService.selectById(information.getColumnId());
        model.addAttribute("columnName",columnType.getName());

        model.addAttribute("item", information);
        LogObjectHolder.me().set(information);
        return PREFIX + "information_edit.html";
    }

    /**
     * 获取资讯管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = informationService.list(condition);
        return super.warpObject(new InformationWarpper(list));
    }

    /**
     * 新增资讯管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Information information) {
        information.setCreateTime(new DateTime());
        information.setSourceId(0);
        information.setPublishIp(ShiroKit.getRandomSalt(5));
        informationService.insert(information);

        //插入标签关联表
        if (information.getTagId() != "") {
            TagRelation tagRelation = new TagRelation();
            tagRelation.setCreateTime(new DateTime());
            tagRelation.setRelationId(information.getId());
            String tagArrId = information.getTagId();
            String[] heyifan = tagArrId.split(",");

            for (int i = 0; i < heyifan.length; i++) {
                tagRelation.setRelationId(information.getId());
                tagRelation.setCreateTime(new DateTime());
                tagRelation.setCommonTypeId(information.getColumnId());
                tagRelation.setColumnId(Integer.parseInt(heyifan[i]));
                tagRelationService.insert(tagRelation);
            }

        }
        return SUCCESS_TIP;
    }

    /**
     * 删除资讯管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer informationId) {
        informationService.deleteById(informationId);
        return SUCCESS_TIP;
    }

    /**
     * 修改资讯管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Information information) {


        //先全部删除标签所有所有管理的表 ,然后在根据前台传入的tagid的数组重新生成标签关联表的数据
        TagRelation tagRelation = new TagRelation();
        EntityWrapper<TagRelation> relationEntityWrapper = new EntityWrapper<>();
        relationEntityWrapper.eq("relation_id", information.getId()).and("common_type_id ={0}",information.getColumnId());
        //先删除之前数据库里面有的标签关联表的数据
        List<TagRelation> tagRelations = tagRelationService.selectList(relationEntityWrapper);
        for (int i = 0; i < tagRelations.size(); i++) {
            tagRelationService.deleteById(tagRelations.get(i).getId());
        }

        //在生成标签管理表
        if (information.getTagId() != "") {
            TagRelation addtagRelation = new TagRelation();
            addtagRelation.setCreateTime(new DateTime());
            addtagRelation.setRelationId(information.getId());
            String tagArrId = information.getTagId();
            String[] heyifan = tagArrId.split(",");

            for (int i = 0; i < heyifan.length; i++) {
                addtagRelation.setRelationId(information.getId());
                addtagRelation.setCreateTime(new DateTime());
                addtagRelation.setCommonTypeId(information.getColumnId());
                addtagRelation.setColumnId(Integer.parseInt(heyifan[i]));
                tagRelationService.insert(addtagRelation);
            }

        }
        information.setUpdateTime(new DateTime());
        informationService.updateById(information);
        return SUCCESS_TIP;
    }

    /**
     * 资讯管理详情
     */
    @RequestMapping(value = "/detail/{informationId}")
    @ResponseBody
    public Object detail(@PathVariable("informationId") Integer informationId) {
        return informationService.selectById(informationId);
    }

    /**
     * 获取栏目类型
     */
    @RequestMapping(value = "/getColumnTypeInformation")
    @ResponseBody
    public Object getColumnTypeInformation() {
        EntityWrapper<ColumnType> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("menu_id ={0}", "1049863045267951793");
        List<ColumnType> columnTypes = columnTypeService.selectList(entityWrapper);
        return columnTypes;
    }

    /**
     * 获取标签数据
     */
    @RequestMapping(value = "/getTagValues/{values}")
    @ResponseBody
    public Object getTagValues(@PathVariable("values") Integer values) {
        EntityWrapper<Tag> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("column_id={0} ", "0").or("column_id={0}",values);
        List<Tag> tags = tagService.selectList(entityWrapper);
        return tags;
    }


}
