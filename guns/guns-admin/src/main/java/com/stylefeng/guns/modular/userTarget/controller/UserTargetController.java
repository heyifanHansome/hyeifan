package com.stylefeng.guns.modular.userTarget.controller;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.FSS;
import com.stylefeng.guns.modular.lijun.util.JavaBeanUtil;
import com.stylefeng.guns.modular.lijun.util.SettingConfiguration;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.model.UserInfo;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.UserTarget;
import com.stylefeng.guns.modular.userTarget.service.IUserTargetService;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 会员等级审核控制器
 *
 * @author fengshuonan
 * @Date 2018-10-17 13:44:27
 */
@Controller
@RequestMapping("/userTarget")
public class UserTargetController extends BaseController {

    private String PREFIX = "/userTarget/userTarget/";
    @Autowired
    private SettingConfiguration settingConfiguration;
    @Autowired
    private Dao dao;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IUserTargetService userTargetService;

    /**
     * 跳转到会员等级审核首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userTarget.html";
    }

    /**
     * 跳转到添加会员等级审核
     */
    @RequestMapping("/userTarget_add")
    public String userTargetAdd() {
        return PREFIX + "userTarget_add.html";
    }

    /**
     * 跳转到修改会员等级审核
     */
    @RequestMapping("/userTarget_update/{userTargetId}")
    public String userTargetUpdate(@PathVariable Integer userTargetId, Model model) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        UserTarget userTarget = userTargetService.selectById(userTargetId);
        Map<String,Object>userTargetMap= JavaBeanUtil.convertBeanToMap(userTarget);
        UserApi userApi=userApiService.selectById(userTarget.getUid());
        userTargetMap.put("user",userApi);
        List<Map<String,Object>>targets=dao.selectBySQL("select id,name from "+ FSS.target+" where pid='0'");
        for (Map<String, Object> target : targets) {
            List<Map<String,Object>>list=dao.selectBySQL("select id,name from "+FSS.target+" where pid='"+target.get("id")+"'");
            for (Map<String, Object> map : list) {
                map.put("targets",dao.selectBySQL("select * from "+FSS.target+" where pid='"+map.get("id")+"'"));
            }
            target.put("targets",list);
        }
        userTargetMap.put("target",JSONArray.fromObject(targets).toString());
        List<Map<String,Object>> thisTargetArray=!Tool.isNull(userTarget.getTarget())&&userTarget.getTarget().startsWith("[")&&userTarget.getTarget().endsWith("]")?JSONArray.fromObject(userTarget.getTarget()):JSONArray.fromObject("[]");
        Set<String> targetSelectIds=new HashSet<>();
        for (Map<String, Object> stringObjectMap : thisTargetArray) {
            if(Tool.mapGetKeyNotEmpty(stringObjectMap,"targets")&&stringObjectMap.get("targets").toString().startsWith("[")&&stringObjectMap.get("targets").toString().endsWith("]")){
                List<Map<String,Object>> thisTargetArray1=JSONArray.fromObject(stringObjectMap.get("targets"));
                for (Map<String, Object> objectMap : thisTargetArray1) {
                    if(Tool.mapGetKeyNotEmpty(objectMap,"targets")&&objectMap.get("targets").toString().startsWith("[")&&objectMap.get("targets").toString().endsWith("]")){
                        List<Map<String,Object>> thisTargetArray2=JSONArray.fromObject(objectMap.get("targets"));
                        for (Map<String, Object> map : thisTargetArray2) {
                            if(!Tool.isNull(map.get("id")))targetSelectIds.add(map.get("id").toString());
                        }
                    }
                }
            }
        }
        userTargetMap.put("targetSelectIds", StringUtils.join(targetSelectIds,","));
        model.addAttribute("item",userTargetMap);
        LogObjectHolder.me().set(userTargetMap);
        return PREFIX + "userTarget_edit.html";
    }

    /**
     * 获取会员等级审核列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> userApis=userApiService.list(condition);
        Set<String>userApiIds=new HashSet<>();
        for (Map<String, Object> userApi : userApis) {
            userApiIds.add(userApi.get("id").toString());
        }
        List<UserTarget>userTargets=new ArrayList<>();
        if (!userApiIds.isEmpty()){
            EntityWrapper<UserTarget>userTargetEntityWrapper=new EntityWrapper<>();
            userTargetEntityWrapper.in("uid",userApiIds);
            userTargetEntityWrapper.orderBy("'check'",true).orderBy("updatetime",false);
            userTargets=userTargetService.selectList(userTargetEntityWrapper);
            for (UserTarget userTarget : userTargets) {
                UserApi userApi=userApiService.selectById(userTarget.getUid());
                userTarget.setUid(userApi!=null?userApi.getName():"<span style='color:red;'>用户不存在</span>");
                userTarget.setCheck("0".equals(userTarget.getCheck())?"<span style='color:red;'>待审核</span>":"1".equals(userTarget.getCheck())?"<span style='color:green;'>已审核</span>":"<span style='color:orange;'>未知状态</span>");
            }
        }
        return userTargets;
    }

    /**
     * 新增会员等级审核
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UserTarget userTarget) {
        userTargetService.insert(userTarget);
        return SUCCESS_TIP;
    }

    /**
     * 删除会员等级审核
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userTargetId) {
        userTargetService.deleteById(userTargetId);
        return SUCCESS_TIP;
    }

    /**
     * 修改会员等级审核
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UserTarget userTarget) {
        userTargetService.updateById(userTarget);
        return SUCCESS_TIP;
    }

    /**
     * 会员等级审核详情
     */
    @RequestMapping(value = "/detail/{userTargetId}")
    @ResponseBody
    public Object detail(@PathVariable("userTargetId") Integer userTargetId) {
        return userTargetService.selectById(userTargetId);
    }

    @RequestMapping("/deleteImg")
    @ResponseBody
    public Object deleteImg(String object_name,String id){
        if(!Tool.isNull(id)) {
            UserTarget userTarget = userTargetService.selectOne(new EntityWrapper<>(new UserTarget()).eq("id", id));
            List<Map<String, Object>> replenish = JSONArray.fromObject(!Tool.isNull(userTarget.getReplenish())&&userTarget.getReplenish().startsWith("[")&&userTarget.getReplenish().endsWith("]")?userTarget.getReplenish():"[]");
//            for (Map<String, Object> rep : replenish) {
            for (Iterator iterator = replenish.iterator(); iterator.hasNext();) {
                Map<String,Object>rep=(Map<String, Object>) iterator.next();
                List<Map<String, Object>>data=JSONArray.fromObject(rep.get("data"));
                for (Iterator it = data.iterator(); it.hasNext(); ) {
                    Map<String, Object> img = (Map<String, Object>) it.next();
                    if (img.get("object_name").equals(object_name)) {
                        it.remove();
                    }
                }
                if(!Tool.listIsNull(data)){
                    rep.put("data",data);
                }else{
                    iterator.remove();
                }
            }
            String replenishJSONString=JSONArray.fromObject(replenish).toString();
            userTarget.setReplenish(replenishJSONString);
            userTargetService.updateById(userTarget);

        }
        Setting setting=settingConfiguration.getSetting();
        OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
        ossClient.deleteObject(setting.getAliOssBucket(), object_name);
        ossClient.shutdown();
        return ResultMsg.success("删除成功",null,null);
    }
}
