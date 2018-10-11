package com.stylefeng.guns.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.model.UserInfo;
import com.stylefeng.guns.modular.system.warpper.UserWorksWarpper;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.service.IUserApiService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author fengshuonan
 * @Date 2018-10-10 11:23:50
 */
@Controller
@RequestMapping("/userApi")
public class UserApiController extends BaseController {

    private String PREFIX = "/system/userApi/";

    @Autowired
    private IUserApiService userApiService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userApi.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/userApi_add")
    public String userApiAdd() {
        return PREFIX + "userApi_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/userApi_update/{userApiId}")
    public String userApiUpdate(@PathVariable Integer userApiId, Model model) {
        UserApi userApi = userApiService.selectById(userApiId);
        model.addAttribute("item", userApi);
        LogObjectHolder.me().set(userApi);
        return PREFIX + "userApi_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {

        List<Map<String, Object>> list  = userApiService.list(condition);
        return super.warpObject(new UserApiWarpper(list));



    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UserApi userApi) {
        userApi.setCreatetime(new DateTime());
        userApiService.insert(userApi);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userApi.getId());
        userInfo.setApiToken(ShiroKit.getRandomSalt(5));
        userInfo.setCredits(0);
        userInfo.setMoney(0);
        userInfo.setLoginIp("192.168.0.0.1");
        userInfo.setCreateTime(new DateTime());
        userInfo.setRealName(userApi.getName());
        userInfo.setJoinClub(2);
        userInfo.setAppointment(2);
        userInfo.setEnlightening(2);
        userInfoService.insert(userInfo);

        return SUCCESS_TIP;
    }

    /**
     * 前台用户删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userApiId) {
        Boolean flag =false;
        //先删除用户详情id
        EntityWrapper<UserInfo> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_id",userApiId);
        List<UserInfo> userInfos = userInfoService.selectList(entityWrapper);
        if(userInfos.size()>0){
         flag =     userInfoService.deleteById(userInfos.get(0).getId());
        }
        if(flag){
            userApiService.deleteById(userApiId);

        }else {
            return  "用户详情删除失败!";
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UserApi userApi) {
        userApi.setUpdatetime(new DateTime());
        userApiService.updateById(userApi);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{userApiId}")
    @ResponseBody
    public Object detail(@PathVariable("userApiId") Integer userApiId) {
        return userApiService.selectById(userApiId);
    }


    /**
     * 详情
     */
    @RequestMapping(value = "/getAllUserApi")
    @ResponseBody
    public Object getAllUserApi() {
        List<UserApi> userApis = userApiService.selectList(null);
        return userApis;
    }

    /**
     * 跳转到用户详情页面页面
     */
    @RequestMapping("/user_info/{userId}")
    public String userInfoDetail(@PathVariable Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        UserApi user = userApiService.selectById(userId);
        EntityWrapper<UserInfo> userInfoEntityWrapper = new EntityWrapper<>();
        userInfoEntityWrapper.eq("user_id", userId);
        List<UserInfo> userInfos = userInfoService.selectList(userInfoEntityWrapper);
        model.addAttribute("item", userInfos.get(0));
        model.addAttribute("userName", user.getName());
        return"/userInfo/userInfo/userInfo_edit.html";
    }

}
