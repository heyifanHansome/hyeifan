package com.stylefeng.guns.modular.works.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.picture.service.IPictureService;
import com.stylefeng.guns.modular.system.model.ColumnType;
import com.stylefeng.guns.modular.system.model.Picture;
import com.stylefeng.guns.modular.system.model.TagRelation;
import com.stylefeng.guns.modular.system.warpper.WorksWarpper;
import com.stylefeng.guns.modular.tagRelation.service.ITagRelationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.Works;
import com.stylefeng.guns.modular.works.service.IWorksService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作品管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 10:44:21
 */
@Controller
@RequestMapping("/works")
public class WorksController extends BaseController {
    /*FuckingCrazying*/
    private String PREFIX = "/works/works/";

    @Autowired
    private IWorksService worksService;

    @Autowired
    private IPictureService pictureService;

    @Autowired
    private IColumnTypeService columnTypeService;

    @Autowired
    private ITagRelationService tagRelationService;



    /**
     * 跳转到作品管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "works.html";
    }

    /**
     * 跳转到添加作品管理
     */
    @RequestMapping("/works_add")
    public String worksAdd() {
        return PREFIX + "works_add.html";
    }

    /**
     * 跳转到修改作品管理
     */
    @RequestMapping("/works_update/{worksId}")
    public String worksUpdate(@PathVariable Integer worksId, Model model) {
        Works works = worksService.selectById(worksId);
        model.addAttribute("item", works);
        EntityWrapper<TagRelation> tagRelationEntityWrapper = new EntityWrapper<>();
        tagRelationEntityWrapper.eq("relation_id",worksId);
        List<TagRelation> tagRelations = tagRelationService.selectList(tagRelationEntityWrapper);
           List<Integer> multArr = new ArrayList<>();
         for (int i = 0; i < tagRelations.size(); i++) {
             Integer temp = tagRelations.get(i).getColumnId();
             multArr.add(temp);
         }
        model.addAttribute("multArr",multArr);
        EntityWrapper<Picture> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("base_id", works.getVideo());
        List<Picture> pictures = pictureService.selectList(entityWrapper);
        List<Map<String, Object>> videoArray = new ArrayList<>();
        JSONObject mapJson = null;
//        videoArray.add("http://cheshi654321.oss-cn-beijing.aliyuncs.com/wocao");
//        videoArray.add("http://cheshi654321.oss-cn-beijing.aliyuncs.com/wocao");
        if (pictures.size() > 0) {
            for (int i = 0; i < pictures.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("key", (pictures.get(i).getOssObjectName()));
//                videoArray.add(pictures.get(i).getOssObjectName());
                mapJson = new JSONObject(map);
                videoArray.add(mapJson);
            }

        } else {

        }
        System.out.println(videoArray);

        model.addAttribute("videoArray", videoArray);
        LogObjectHolder.me().set(works);
        return PREFIX + "works_edit.html";
    }

    /**
     * 获取作品管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = worksService.list(condition);
        return super.warpObject(new WorksWarpper(list));
    }

    /**
     * 新增作品管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Works works) {
        Integer columnId =null;
        works.setCreateTime(new DateTime());
        worksService.insert(works);
        TagRelation tagRelation = new TagRelation();
        tagRelation.setCreateTime(new DateTime());
        tagRelation.setRelationId(works.getId());
        String tagArrId = works.getTagId();

        String[] heyifan =tagArrId.split(",");

        EntityWrapper<ColumnType> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("name", "活动");
        List<ColumnType> columnTypes = columnTypeService.selectList(entityWrapper);
        if (columnTypes.size() > 0) {
          columnId =    columnTypes.get(0).getId();
        }

        for (int i = 0; i < heyifan.length; i++) {
            tagRelation.setRelationId(works.getId());
            tagRelation.setCreateTime(new DateTime());
            tagRelation.setCommonTypeId(columnId);
            tagRelation.setColumnId(Integer.parseInt(heyifan[i]));
            tagRelationService.insert(tagRelation);
        }

        return SUCCESS_TIP;
    }

    /**
     * 删除作品管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer worksId) {
        worksService.deleteById(worksId);
        return SUCCESS_TIP;
    }

    /**
     * 修改作品管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Works works) {
        Integer columnId =null;
        TagRelation tagRelation = new TagRelation();
            EntityWrapper<TagRelation>  relationEntityWrapper = new EntityWrapper<>();
            relationEntityWrapper.eq("relation_id",works.getId());
            List<TagRelation> tagRelations = tagRelationService.selectList(relationEntityWrapper);
            for (int i = 0; i < tagRelations.size(); i++) {
                tagRelationService.deleteById(tagRelations.get(i).getId());
        }
        if(works.getTagId()==""){
        }else{
            String tagArrId = works.getTagId();
            String[] heyifan =tagArrId.split(",");
            EntityWrapper<ColumnType> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("name", "活动");
            List<ColumnType> columnTypes = columnTypeService.selectList(entityWrapper);
            if (columnTypes.size() > 0) {
                columnId =    columnTypes.get(0).getId();
            }

            for (int i = 0; i < heyifan.length; i++) {
                tagRelation.setRelationId(works.getId());
                tagRelation.setCreateTime(new DateTime());
                tagRelation.setCommonTypeId(columnId);
                tagRelation.setColumnId(Integer.parseInt(heyifan[i]));
                tagRelationService.insert(tagRelation);
            }
        }



        works.setUpdateTime(new DateTime());
        worksService.updateById(works);
        return SUCCESS_TIP;
    }


    @GetMapping(value = "/img")
    @ResponseBody
    public ResponseEntity<?> getUser(String baseId) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            EntityWrapper<Picture> entityWrapper = new EntityWrapper<>();
            entityWrapper.like("base_id", baseId);
            List<Picture> picture = pictureService.selectList(entityWrapper);
            StringBuffer listObject = new StringBuffer();

            for (int i = 0; i < picture.size(); i++) {
                listObject.append("," + picture.get(i).getOssObjectName());
            }

            resultMsg = ResultMsg.success("查询成功", "查询成功", listObject);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ResultMsg>(ResultMsg.fail("系统错误", "系统错误", ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ResultMsg>(resultMsg, HttpStatus.OK);

    }


    /**
     * 获取所有用户
     */
    @RequestMapping(value = "/getAllWorks")
    @ResponseBody
    public List<Works> getAllWorks() {
        List<Works> Works = worksService.selectList(null);
        return Works;
    }


    /**
     * 获取所有用户
     */
    @RequestMapping(value = "/getAllColumnType")
    @ResponseBody
    public List<ColumnType> getAllColumnwType() {
        List<ColumnType> ColumnType = columnTypeService.selectList(null);
        return ColumnType;
    }

    /**
     * 作品管理详情
     */
    @RequestMapping(value = "/detail/{worksId}")
    @ResponseBody
    public Object detail(@PathVariable("worksId") Integer worksId) {



        return worksService.selectById(worksId);
    }


    public String deleteObjectName(HttpServletRequest request, HttpServletResponse response) {
        ResultMsg resultMsg = new ResultMsg();
        String id = (String) request.getParameter("key");//获取图片id
        EntityWrapper<Picture> entityWrapper = new EntityWrapper();
        entityWrapper.like("oss_object_name", id);
        List<Picture> pictures = pictureService.selectList(entityWrapper);
        return "";
    }
}
