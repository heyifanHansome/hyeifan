package com.stylefeng.guns.modular.works.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.annotion.Permission;
import com.stylefeng.guns.core.common.constant.Const;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.lijun.util.SettingConfiguration;
import com.stylefeng.guns.modular.picture.service.IPictureService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.warpper.WorksWarpper;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.tagRelation.service.ITagRelationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
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
private SettingConfiguration settingConfiguration;
    @Autowired
    private IWorksService worksService;

    @Autowired
    private IPictureService pictureService;

    @Autowired
    private IColumnTypeService columnTypeService;

    @Autowired
    private ITagRelationService tagRelationService;

    @Autowired
    private ITagService tagService;


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
        /**
         *回显标签定义
         */
        model.addAttribute("item", works);
        EntityWrapper<TagRelation> tagRelationEntityWrapper = new EntityWrapper<>();
        tagRelationEntityWrapper.eq("relation_id", worksId);
        List<TagRelation> tagRelations = tagRelationService.selectList(tagRelationEntityWrapper);
        List<Integer> multArr = new ArrayList<>();
        for (int i = 0; i < tagRelations.size(); i++) {
            Integer temp = tagRelations.get(i).getColumnId();
            multArr.add(temp);
        }
        model.addAttribute("multArr", multArr);

        /**
         *回显图片数组定义
         */
        EntityWrapper<Picture> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("base_id", works.getVideo());
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
        Integer columnId = null;
        works.setCreateTime(new DateTime());
        EntityWrapper<ColumnType> columnTypeEntityWrapper = new EntityWrapper<>();
        columnTypeEntityWrapper.eq("name", "活动");
        List<ColumnType> columnTypes = columnTypeService.selectList(columnTypeEntityWrapper);
        works.setColumnId(columnTypes.get(0).getId());
        worksService.insert(works);
        if (works.getTagId() != "") {
            TagRelation tagRelation = new TagRelation();
            tagRelation.setCreateTime(new DateTime());
            tagRelation.setRelationId(works.getId());
            String tagArrId = works.getTagId();
            String[] heyifan = tagArrId.split(",");
            if (columnTypes.size() > 0) {
                columnId = columnTypes.get(0).getId();
            }

            for (int i = 0; i < heyifan.length; i++) {
                tagRelation.setRelationId(works.getId());
                tagRelation.setCreateTime(new DateTime());
                tagRelation.setCommonTypeId(columnId);
                tagRelation.setColumnId(Integer.parseInt(heyifan[i]));
                tagRelationService.insert(tagRelation);
            }

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
    @Permission(Const.ADMIN_NAME)
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Works works)  {

        Integer columnId = null;
        TagRelation tagRelation = new TagRelation();
        EntityWrapper<TagRelation> relationEntityWrapper = new EntityWrapper<>();
        relationEntityWrapper.eq("relation_id", works.getId());
        List<TagRelation> tagRelations = tagRelationService.selectList(relationEntityWrapper);
        for (int i = 0; i < tagRelations.size(); i++) {
            tagRelationService.deleteById(tagRelations.get(i).getId());
        }
        if (works.getTagId() == "") {
        } else {
            String tagArrId = works.getTagId();
            String[] heyifan = tagArrId.split(",");
            EntityWrapper<ColumnType> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("name", "活动");
            List<ColumnType> columnTypes = columnTypeService.selectList(entityWrapper);
            if (columnTypes.size() > 0) {
                columnId = columnTypes.get(0).getId();
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
        EntityWrapper<ColumnType> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("name","课堂");
        List<ColumnType> ColumnTypes = columnTypeService.selectList(entityWrapper);
        EntityWrapper<ColumnType> columnTypeEntityWrapper = new EntityWrapper<>();
        columnTypeEntityWrapper.eq("menu_id",    ColumnTypes.get(0).getId());
        List<ColumnType> parentColumnTypes = columnTypeService.selectList(columnTypeEntityWrapper);


        return parentColumnTypes;
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

    /*
     * 删除上传的视频
     * @param objectName 传入上传路径
     */
@RequestMapping(value = "/deleteVideoByObjectName")
@ResponseBody
public  Object deleteVideoByObjectName (String objectName){
    Setting setting=settingConfiguration.getSetting();
    OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
    int index   = objectName.indexOf("/data");
    String newString =  objectName.substring(index+1);
    ossClient.deleteObject(setting.getAliOssBucket(), newString);

    ossClient.shutdown();

    /*再删除对应baseid的视频*/
    EntityWrapper<Picture> pictureEntityWrapper = new EntityWrapper<>();
    pictureEntityWrapper.eq("oss_object_name",objectName);
    List<Picture> pictures = pictureService.selectList(pictureEntityWrapper);
    for (int i = 0; i < pictures.size(); i++) {
            pictureService.deleteById(pictures.get(i).getId());
    }
    return "返回成功!";
}


    /**
     *  审核视频
     * @param objectName 文件名称
     * @return 审核结果
     */
    @RequestMapping(value = "/checkVideo")
    @ResponseBody
    public boolean checkVideo(String objectName){
        try {
            EntityWrapper<Picture> pictureEntityWrapper = new EntityWrapper<>();
            pictureEntityWrapper.eq("oss_object_name",objectName);
            List<Picture> pictures = pictureService.selectList(pictureEntityWrapper);
            Picture picture =    pictures.get(0);
            picture.setCheck(1);
            pictureService.updateById(picture);
        }catch (Exception e ){
            e.printStackTrace();
            return  false;
        }
     return true;
    }



    /**
     * 联动标签
     */
    @RequestMapping(value = "/getCurrentTag")
    @ResponseBody
    public List<Tag> getCurrentTag() {
        EntityWrapper<Tag> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("column_id ={0} or column_id={1}",0,17 );
        List<Tag> tags = tagService.selectList(entityWrapper);
        return  tags;
    }




}
