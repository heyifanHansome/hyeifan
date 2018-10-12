package com.stylefeng.guns.modular.classroom.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.picture.service.IPictureService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.warpper.ClassroomWarpper;
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
import com.stylefeng.guns.modular.classroom.service.IClassroomService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 星厨课堂控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 10:15:18
 */
@Controller
@RequestMapping("/classroom")
public class ClassroomController extends BaseController {

    private String PREFIX = "/classroom/classroom/";

    @Autowired
    private IClassroomService classroomService;

    @Autowired
    private IPictureService pictureService;
    @Autowired
    private ITagService tagService;

    @Autowired
    private ITagRelationService tagRelationService;

    @Autowired
    private IColumnTypeService columnTypeService;

    /**
     * 跳转到星厨课堂首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "classroom.html";
    }

    /**
     * 跳转到添加星厨课堂
     */
    @RequestMapping("/classroom_add")
    public String classroomAdd() {
        return PREFIX + "classroom_add.html";
    }

    /**
     * 跳转到修改星厨课堂
     */
    @RequestMapping("/classroom_update/{classroomId}")
    public String classroomUpdate(@PathVariable Integer classroomId, Model model) {
        Classroom classroom = classroomService.selectById(classroomId);

        /**
         * 获取前台需要展示的样式 1为视频集合,2为图片集
         */

        if (classroom.getColumnId() != 27) {
            model.addAttribute("modelType", "2");
        } else {
            model.addAttribute("modelType", "1");
        }

        /**
         *获取当前的视频集合
         */
        EntityWrapper<Picture> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("base_id", classroom.getVideo());
        List<Picture> pictures = pictureService.selectList(entityWrapper);
        List<Map<String, Object>> videoArray = new ArrayList<>();
        JSONObject mapJson = null;
        if (pictures.size() > 0) {
            for (int i = 0; i < pictures.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("key", (pictures.get(i).getOssObjectName()));
                mapJson = new JSONObject(map);
                videoArray.add(mapJson);
            }

        } else {

        }

        model.addAttribute("videoArray", videoArray);

        model.addAttribute("item", classroom);
        LogObjectHolder.me().set(classroom);
        return PREFIX + "classroom_edit.html";
    }

    /**
     * 获取星厨课堂列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = classroomService.list(condition);

        return super.warpObject(new ClassroomWarpper(list));
    }

    /**
     * 新增星厨课堂
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Classroom classroom) {
        classroom.setCreateTime(new DateTime());
        classroomService.insert(classroom);

        //插入标签关联表
        if (classroom.getTagId() != "") {
            TagRelation tagRelation = new TagRelation();
            tagRelation.setCreateTime(new DateTime());
            tagRelation.setRelationId(classroom.getId());
            String tagArrId = classroom.getTagId();
            String[] heyifan = tagArrId.split(",");

            for (int i = 0; i < heyifan.length; i++) {
                tagRelation.setRelationId(classroom.getId());
                tagRelation.setCreateTime(new DateTime());
                tagRelation.setCommonTypeId(27);
                tagRelation.setColumnId(Integer.parseInt(heyifan[i]));
                tagRelationService.insert(tagRelation);
            }

        }
        return SUCCESS_TIP;
    }

    /**
     * 删除星厨课堂
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer classroomId) {
        classroomService.deleteById(classroomId);
        return SUCCESS_TIP;
    }

    /**
     * 修改星厨课堂
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Classroom classroom) {
        classroom.setUpdateTime(new DateTime());
        classroomService.updateById(classroom);
        return SUCCESS_TIP;
    }

    /**
     * 获取视频标签管理表
     */
    @RequestMapping(value = "/getClassRoom")
    @ResponseBody
    public Object getClassRoom() {
        EntityWrapper<Tag> tagEntityWrapper = new EntityWrapper<>();
        tagEntityWrapper.where("column_id={0}", "0").or("  column_id={0}", "17");
        List<Tag> tags = tagService.selectList(tagEntityWrapper);
        System.err.println(tags);
        return tags;
    }

    /**
     * 星厨课堂详情
     */
    @RequestMapping(value = "/detail/{classroomId}")
    @ResponseBody
    public Object detail(@PathVariable("classroomId") Integer classroomId) {
        return classroomService.selectById(classroomId);
    }

    /**
     * 获取栏目详情
     */
    @RequestMapping(value = "/getAllColumunType")
    @ResponseBody
    public List<ColumnType> getAllColumunType() {
        EntityWrapper<ColumnType> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("menu_id","1049863045267951779");
        List<ColumnType> columnTypes = columnTypeService.selectList(entityWrapper);
        return  columnTypes;
    }


    /**
     * 联动标签
     */
    @RequestMapping(value = "/getCurrentTag/{values}")
    @ResponseBody
    public List<Tag> getCurrentTag(@PathVariable("values") Integer values) {
        EntityWrapper<Tag> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("column_id ={0} or column_id={1}","0",values) ;
        List<Tag> tags = tagService.selectList(entityWrapper);
        return  tags;
    }
}
