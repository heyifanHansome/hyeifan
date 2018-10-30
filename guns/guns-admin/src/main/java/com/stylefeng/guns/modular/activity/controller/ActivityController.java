package com.stylefeng.guns.modular.activity.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.city.service.ICityService;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.lijun.util.SettingConfiguration;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.tag.service.ITagService;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.activity.service.IActivityService;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 活动管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-21 10:32:49
 */
@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseController {

    private String PREFIX = "/activity/activity/";
    @Autowired
    private SettingConfiguration settingConfiguration;
    @Autowired
    private IActivityService activityService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserApiService userApiService;
    /**
     * 跳转到活动管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "activity.html";
    }

    /**
     * 跳转到添加活动管理
     */
    @RequestMapping("/activity_add")
    public String activityAdd(Model model,String menu_id) {
        model.addAttribute("menu_id",menu_id);
        return PREFIX + "activity_add.html";
    }

    /**
     * 跳转到修改活动管理
     */
    @RequestMapping("/activity_update/{activityId}")
    public String activityUpdate(@PathVariable Integer activityId, Model model,String menu_id) {
        Activity activity = activityService.selectById(activityId);
        switch (activity.getSourceId()){
            case "0":
                User user=new User();
                user.setId(Integer.valueOf(activity.getUid()));
                EntityWrapper<User>userEntityWrapper=new EntityWrapper<>(user);
                user=userService.selectOne(userEntityWrapper);
                activity.setUid(((user!=null?user.getName():"<span style='color:red;'>该管理员已被删除</span>")+"<span style='color:red;'>(管理员)</span>"));
                break;
            case "1":
                UserApi api=new UserApi();
                api.setId(Integer.valueOf(activity.getUid()));
                EntityWrapper<UserApi>apiEntityWrapper=new EntityWrapper<>(api);
                api=userApiService.selectOne(apiEntityWrapper);
                activity.setUid(((api!=null?api.getName():"<span style='color:red;'>该用户已被删除</span>")+"<span style='color:red;'>(用户)</span>"));
                break;
            default:
                activity.setUid(("<span style='color:red;'>(未知角色)</span>"));
                break;
        }
        model.addAttribute("startTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(activity.getStartTime()!=null?activity.getStartTime():new java.util.Date()));
        model.addAttribute("menu_id",menu_id);
        model.addAttribute("item", activity);
        LogObjectHolder.me().set(activity);
        return PREFIX + "activity_edit.html";
    }

    /**
     * 获取活动管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = activityService.list(condition);
        for (Map<String, Object> activity : list) {
            String[] tagIds=!Tool.isNull(activity.get("tag_id"))?activity.get("tag_id").toString().trim().split(","):new String[]{};
            for (int i = 0; i < tagIds.length; i++) {
                if("-1".equals(tagIds[i].trim()))activity.put("title",activity.get("title")+"<span style='color:red;'>(精)</span>");
                if("-2".equals(tagIds[i].trim()))activity.put("title",activity.get("title")+"<span style='color:red;'>(热)</span>");
            }
            City city=cityService.selectOne(new EntityWrapper<>(new City()).eq("id",activity.get("city_id")));
            activity.put("city_id",city!=null&&!Tool.isNull(city.getName())?city.getName():"<span style='color:red'>*所选城市不存在*</span>");
            if(!Tool.isNull(activity.get("source_id"))){
                switch (activity.get("source_id").toString()){
                    case "0":activity.put("source_id","官方");
                        break;
                    case "1":activity.put("source_id","个人");
                        break;
                    default:activity.put("source_id","其他");
                        break;
                }
            }else{
                activity.put("source_id","<span style='color:red'>*空*</span>");
            }
            if(!Tool.isNull(activity.get("is_ok"))){
                switch (activity.get("is_ok").toString()){
                    case "0":activity.put("is_ok","待审核");
                        break;
                    case "1":activity.put("is_ok","通过");
                        break;
                    case "2":activity.put("is_ok","拒绝");
                        break;
                    default:activity.put("is_ok","未知");
                        break;
                }
            }else{
                activity.put("is_ok","<span style='color:red'>*空*</span>");
            }
        }
        return list;
    }

    /**
     * 新增活动管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Activity activity,String old_object_name) {
        Setting setting=settingConfiguration.getSetting();
        OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
        if(!Tool.isNull(old_object_name))
        ossClient.deleteObject(setting.getAliOssBucket(), old_object_name);
        ossClient.shutdown();
        activity.setUid(String.valueOf(ShiroKit.getUser().getId()));
        activity.setPublishIp(Tool.getIpAdrress());
        activity.setCreateTime(new Date(System.currentTimeMillis()));
        activityService.insert(activity);
        return SUCCESS_TIP;
    }

    /**
     * 删除活动管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer activityId) {
        Activity activity = activityService.selectById(activityId);
        Setting setting=settingConfiguration.getSetting();
        OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());        if(!Tool.isNull(activity.getVideo_object_name()))
        ossClient.deleteObject(setting.getAliOssBucket(), activity.getVideo_object_name());
        List<Map<String, Object>> imgs = JSONArray.fromObject(activity.getThumb());
        List<String>img_objects=new ArrayList<>();
        for (Iterator it = imgs.iterator(); it.hasNext(); ) {
            img_objects.add(((Map<String, Object>) it.next()).get("object_name").toString());
        }
        if(!Tool.listIsNull(img_objects))ossClient.deleteObjects(new DeleteObjectsRequest(setting.getAliOssBucket()).withKeys(img_objects));
        ossClient.shutdown();
        activityService.deleteById(activityId);
        return SUCCESS_TIP;
    }

    /**
     * 修改活动管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Activity activity,String old_object_name) {
        Setting setting=settingConfiguration.getSetting();
        OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());        if(!Tool.isNull(old_object_name))
        ossClient.deleteObject(setting.getAliOssBucket(), old_object_name);
        ossClient.shutdown();
        activity.setUid(String.valueOf(ShiroKit.getUser().getId()));
        activity.setPublishIp(Tool.getIpAdrress());
        activity.setUpdateTime(new Date(System.currentTimeMillis()));
        activityService.updateById(activity);
        return SUCCESS_TIP;
    }

    /**
     * 活动管理详情
     */
    @RequestMapping(value = "/detail/{activityId}")
    @ResponseBody
    public Object detail(@PathVariable("activityId") Integer activityId) {
        return activityService.selectById(activityId);
    }

    @RequestMapping("/deleteImg")
    @ResponseBody
    public Object deleteImg(String object_name,String id){
        if(!Tool.isNull(id)) {
            Activity activity = activityService.selectOne(new EntityWrapper<>(new Activity()).eq("id", id));
            List<Map<String, Object>> imgs = JSONArray.fromObject(activity.getThumb());
            for (Iterator it = imgs.iterator(); it.hasNext(); ) {
                Map<String, Object> img = (Map<String, Object>) it.next();
                if (img.get("object_name").equals(object_name)) {
                    it.remove();
                }
            }
            String imgsJSONString=JSONArray.fromObject(imgs).toString();
            activity.setThumb(imgsJSONString);
            activityService.updateById(activity);
        }
        Setting setting=settingConfiguration.getSetting();
        OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
        ossClient.deleteObject(setting.getAliOssBucket(), object_name);
        ossClient.shutdown();
        return ResultMsg.success("删除成功",null,null);
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
        List<Tag>tags=new ArrayList<>();
        if(tagEntityWrapper!=null)tags=tagService.selectList(tagEntityWrapper);
        tags.addAll(tagService.selectList(tagEntityWrapper==null?new EntityWrapper<Tag>().eq("column_id","0"):tagEntityWrapper.eq("column_id","0")));
        return tags;
    }
}
