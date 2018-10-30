package com.stylefeng.guns.modular.banner.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.lijun.util.FSS;
import com.stylefeng.guns.modular.lijun.util.SettingConfiguration;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.ColumnType;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.tag.service.ITagService;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Banner;
import com.stylefeng.guns.modular.banner.service.IBannerService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 广告管理控制器
 *
 * @author fengshuonan
 * @Date 2018-10-22 17:35:07
 */
@Controller
@RequestMapping("/banner")
public class BannerController extends BaseController {

    private String PREFIX = "/banner/banner/";
    @Autowired
    private SettingConfiguration settingConfiguration;
    @Autowired
    private IBannerService bannerService;

    /**
     * 跳转到广告管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "banner.html";
    }

    /**
     * 跳转到添加广告管理
     */
    @RequestMapping("/banner_add")
    public String bannerAdd(Model model,String menu_id) {
        model.addAttribute("menu_id",menu_id);
        return PREFIX + "banner_add.html";
    }

    /**
     * 跳转到修改广告管理
     */
    @RequestMapping("/banner_update/{bannerId}")
    public String bannerUpdate(@PathVariable Integer bannerId, Model model,String menu_id) {
        Banner banner = bannerService.selectById(bannerId);
        model.addAttribute("item",banner);
        model.addAttribute("menu_id",menu_id);
        LogObjectHolder.me().set(banner);
        return PREFIX + "banner_edit.html";
    }

    /**
     * 获取广告管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Banner>banners=bannerService.selectList(null);
        for (Banner banner : banners) {
            String[] tagIds=banner.getTagId()!=null?banner.getTagId().trim().split(","):new String[]{};
            for (int i = 0; i < tagIds.length; i++) {
                if("-1".equals(tagIds[i].trim()))banner.setTitle(banner.getTitle()+"<span style='color:red;'>(精)</span>");
                if("-2".equals(tagIds[i].trim()))banner.setTitle(banner.getTitle()+"<span style='color:red;'>(热)</span>");
            }
            banner.setIs_ok("1".equals(banner.getIs_ok())?"是":"否");
//            if(!Tool.isNull(banner.getTagId())){
//                List<Map<String,Object>>tags=dao.selectBySQL("select * from "+FSS.banner+" where id in("+banner.getTagId()+")");
//                List<String>tagNames=new ArrayList<>();
//                for (Map<String, Object> tag : tags) {
//                    if(!Tool.isNull(tag.get("name")))tagNames.add(tag.get("nasme").toString());
//                }
//                banner.setTagId(StringUtils.join(tagNames,","));
//            }
            switch (banner.getType()){
                case FSS.classroom:
                    List<Map<String,Object>>items=!Tool.isNull(banner.getItem_id())?dao.selectBySQL("select title from "+banner.getType()+" where id="+banner.getItem_id()):new ArrayList<>();
                    banner.setItem_id("星厨课堂:"+(Tool.isNull(banner.getItem_id())?"<span style='color:red;'>未指定广告对象,请指定广告对象</span>":!Tool.listIsNull(items)?items.get(0).get("title"):"<span style='color:red;'>指定的广告对象已被删除,请清理掉本条广告,或者为本条广告指定其他的广告对象</span>"));
                    break;
                case FSS.banner:banner.setItem_id("广告");
                    break;
                default:banner.setItem_id("<span style='color:red;'>未知</span>");
                    break;
            }
            if(!Tool.isNull(banner.getPicture())){
                banner.setPicture("<img src='"+banner.getPicture()+"' style='width:100%;'/>");
            }
        }
        return banners;
    }

    /**
     * 新增广告管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Banner banner,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            Setting setting=settingConfiguration.getSetting();
            OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
            ossClient.deleteObject(setting.getAliOssBucket(), old_object_name);
            ossClient.shutdown();
        }
        bannerService.insert(banner);
        return SUCCESS_TIP;
    }

    /**
     * 删除广告管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bannerId) {
        Banner banner = bannerService.selectOne(new EntityWrapper<>(new Banner()).eq("id", bannerId));
        Setting setting=settingConfiguration.getSetting();
        OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
        if(!Tool.isNull(banner.getObject_name()))ossClient.deleteObject(setting.getAliOssBucket(),banner.getObject_name());
        ossClient.shutdown();
        bannerService.deleteById(bannerId);
        return SUCCESS_TIP;
    }

    /**
     * 修改广告管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Banner banner,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            Setting setting=settingConfiguration.getSetting();
            OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
            ossClient.deleteObject(setting.getAliOssBucket(), old_object_name);
            ossClient.shutdown();
        }
        bannerService.updateById(banner);
        return SUCCESS_TIP;
    }

    /**
     * 广告管理详情
     */
    @RequestMapping(value = "/detail/{bannerId}")
    @ResponseBody
    public Object detail(@PathVariable("bannerId") Integer bannerId) {
        return bannerService.selectById(bannerId);
    }

    @Autowired
    private ITagService tagService;
    @Autowired
    private IColumnTypeService columnTypeService;
    @RequestMapping("getTag")
    @ResponseBody
    public Object getTag(String menu_id){
        EntityWrapper<ColumnType>columnTypeEntityWrapper=new EntityWrapper<>();
        columnTypeEntityWrapper.eq("menu_id",menu_id);
        List<ColumnType> columnTypeList=columnTypeService.selectList(columnTypeEntityWrapper);
        EntityWrapper<Tag>tagEntityWrapper=null;
        if(!Tool.listIsNull(columnTypeList)){
            tagEntityWrapper=new EntityWrapper<>();
            List<Integer>columnTypeIDs=new ArrayList<>();
            for (ColumnType columnType : columnTypeList) {
                columnTypeIDs.add(columnType.getId());
            }
            columnTypeIDs.add(0);
            tagEntityWrapper.in("column_id",columnTypeIDs);
        }
        return tagEntityWrapper!=null?tagService.selectList(tagEntityWrapper):new ArrayList<Tag>() {};
    }

    @Autowired
    private Dao dao;
    @RequestMapping("getItemByType")
    @ResponseBody
    public Object getItemByType(String type){
        List<Map<String,Object>>result=new ArrayList<>();
        if(FSS.classroom.equals(type)){
            result=dao.selectBySQL("select id,title as name from "+ type);
        }
        return result;
    }
}
