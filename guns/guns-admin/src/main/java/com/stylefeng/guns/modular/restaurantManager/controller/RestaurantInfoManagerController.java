package com.stylefeng.guns.modular.restaurantManager.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.city.service.ICityService;
import com.stylefeng.guns.modular.lijun.util.FinalStaticString;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.City;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.RestaurantInfoManager;
import com.stylefeng.guns.modular.restaurantManager.service.IRestaurantInfoManagerService;

import java.util.*;

/**
 * 餐厅信息管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 11:30:22
 */
@Controller
@RequestMapping("/restaurantInfoManager")
public class RestaurantInfoManagerController extends BaseController {

    private String PREFIX = "/restaurantManager/restaurantInfoManager/";

    @Autowired
    private IRestaurantInfoManagerService restaurantInfoManagerService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ICityService cityService;


    /**
     * 跳转到餐厅信息管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "restaurantInfoManager.html";
    }

    /**
     * 跳转到添加餐厅信息管理
     */
    @RequestMapping("/restaurantInfoManager_add")
    public String restaurantInfoManagerAdd() {
        return PREFIX + "restaurantInfoManager_add.html";
    }

    /**
     * 跳转到修改餐厅信息管理
     */
    @RequestMapping("/restaurantInfoManager_update/{restaurantInfoManagerId}")
    public String restaurantInfoManagerUpdate(@PathVariable Integer restaurantInfoManagerId, Model model) {
        RestaurantInfoManager restaurantInfoManager = restaurantInfoManagerService.selectById(restaurantInfoManagerId);
        model.addAttribute("item",restaurantInfoManager);
        LogObjectHolder.me().set(restaurantInfoManager);
        return PREFIX + "restaurantInfoManager_edit.html";
    }

    /**
     * 获取餐厅信息管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<RestaurantInfoManager>restaurantInfoManagers=restaurantInfoManagerService.selectList(null);
        for (RestaurantInfoManager restaurantInfoManager : restaurantInfoManagers) {
            EntityWrapper<User>userEntityWrapper=new EntityWrapper<>();
            userEntityWrapper.eq("id",Integer.valueOf(restaurantInfoManager.getUserId()));
            User user=userService.selectOne(userEntityWrapper);
            restaurantInfoManager.setUserId(user!=null?user.getName():"<span style='color:red;'>*用户不存在*</span>");

            EntityWrapper<City>cityEntityWrapper=new EntityWrapper<>();
            cityEntityWrapper.eq("id",Integer.valueOf(restaurantInfoManager.getCityId()));
            City city=cityService.selectOne(cityEntityWrapper);
            restaurantInfoManager.setCityId(city!=null?city.getName():"<span style='color:red;'>*城市不存在*</span>");

            switch (restaurantInfoManager.getStatus()){
                case ("0"):restaurantInfoManager.setStatus("未审查");
                    break;
                case ("1"):restaurantInfoManager.setStatus("考察");
                    break;
                case ("2"):restaurantInfoManager.setStatus("通过");
                    break;
                case ("3"):restaurantInfoManager.setStatus("拒绝");
                    break;
                default:restaurantInfoManager.setStatus("未知");
                    break;
            }
        }
        return restaurantInfoManagers;
    }

    /**
     * 新增餐厅信息管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(RestaurantInfoManager restaurantInfoManager,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
            ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET, old_object_name);
            ossClient.shutdown();
        }
        restaurantInfoManager.setCreateTime(new Date(System.currentTimeMillis()));
        restaurantInfoManagerService.insert(restaurantInfoManager);
        return SUCCESS_TIP;
    }

    /**
     * 删除餐厅信息管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer restaurantInfoManagerId) {
        RestaurantInfoManager restaurantInfoManager = restaurantInfoManagerService.selectOne(new EntityWrapper<>(new RestaurantInfoManager()).eq("id", restaurantInfoManagerId));
        List<Map<String, Object>> imgs = JSONArray.fromObject(restaurantInfoManager.getImages());
        OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
        List<String>img_objects=new ArrayList<>();
        for (Iterator it = imgs.iterator(); it.hasNext(); ) {
            img_objects.add(((Map<String, Object>) it.next()).get("object_name").toString());
        }
        if(!Tool.isNull(restaurantInfoManager.getObject_name()))ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET,restaurantInfoManager.getObject_name());
        if(!Tool.listIsNull(img_objects))ossClient.deleteObjects(new DeleteObjectsRequest(FinalStaticString.ALI_OSS_BUCKET).withKeys(img_objects));
        ossClient.shutdown();
        restaurantInfoManagerService.deleteById(restaurantInfoManagerId);
        return SUCCESS_TIP;
    }

    /**
     * 修改餐厅信息管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(RestaurantInfoManager restaurantInfoManager,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
            ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET, old_object_name);
            ossClient.shutdown();
        }
        restaurantInfoManager.setUpdateTime(new Date(System.currentTimeMillis()));
        restaurantInfoManagerService.updateById(restaurantInfoManager);
        return SUCCESS_TIP;
    }

    /**
     * 餐厅信息管理详情
     */
    @RequestMapping(value = "/detail/{restaurantInfoManagerId}")
    @ResponseBody
    public Object detail(@PathVariable("restaurantInfoManagerId") Integer restaurantInfoManagerId) {
        return restaurantInfoManagerService.selectById(restaurantInfoManagerId);
    }

    @RequestMapping("/deleteImg")
    @ResponseBody
    public Object deleteImg(String object_name,String id){
        if(!Tool.isNull(id)) {
            RestaurantInfoManager restaurantInfoManager = restaurantInfoManagerService.selectOne(new EntityWrapper<>(new RestaurantInfoManager()).eq("id", id));
            List<Map<String, Object>> imgs = JSONArray.fromObject(restaurantInfoManager.getImages());
            for (Iterator it = imgs.iterator(); it.hasNext(); ) {
                Map<String, Object> img = (Map<String, Object>) it.next();
                if (img.get("object_name").equals(object_name)) {
                    it.remove();
                }
            }
            String imgsJSONString=JSONArray.fromObject(imgs).toString();
            restaurantInfoManager.setImages(imgsJSONString);
            restaurantInfoManagerService.updateById(restaurantInfoManager);
        }
        OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
        ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET, object_name);
        ossClient.shutdown();
        return ResultMsg.success("删除成功",null,null);
    }
}
