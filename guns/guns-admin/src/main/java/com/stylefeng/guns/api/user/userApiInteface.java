package com.stylefeng.guns.api.user;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.*;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.model.UserInfo;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.systemSetting.service.ISettingService;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by Heyifan Cotter on 2018/10/13.
 */

/**
 * 前台用户登录接口实现类
 */
@RestController
@RequestMapping("userApi")
public class userApiInteface {
@Autowired
private IUserApiService userApiService;
@Autowired
private IUserInfoService userInfoService;
@Autowired
private ISettingService settingService;
    /**
     * 注册用户发送验证码
     * @param phone 前台传入的手机号
     * @return  发送成功或者失败的信息
     */
    @RequestMapping("getVerificationCode")
    @ResponseBody
    public ResultMsg getVerificationCode(String phone){
        int randomCode= Tool.getRandomNum();
        try {
            String str=DuanXin_LiJun.tplSendSms(String.valueOf(randomCode),phone);
            Map<String,Object>result= JSONObject.fromObject(str);
            if(!Tool.isNull(result.get("code"))){
                if("0".equals(result.get("code"))){
                    ((HttpSession)Tool.getRequest_Response_Session()[2]).setAttribute(FinalStaticString.LOGIN_CODE,randomCode);
                    return ResultMsg.success("发送成功!",result.toString(),null);
                }else{
                    return ResultMsg.fail("发送失败",result.toString(),null);
                }
            }else{
                return ResultMsg.fail("发送失败",result.toString(),null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMsg.fail("发送失败",e.toString(),null);
        }
    }

    /**
     * 根据用户手机号和验证码登录并且记录用户信息
     * @param phone 手机号
     * @param code 验证码
     * @return
     */
    @RequestMapping("userLogin")
    @ResponseBody
    public ResultMsg userLogin(String phone,String code){
        try {
            Object session_code=((HttpSession)Tool.getRequest_Response_Session()[2]).getAttribute(FinalStaticString.LOGIN_CODE);
            if(Tool.isNull(phone))return ResultMsg.fail("请输入手机号",null,null);
            if(!"localhost".equals(Tool.getDomain())&&!"192.168.1.21".equals(Tool.getDomain())&&!"192.168.1.28".equals(Tool.getDomain())){
                if(Tool.isNull(session_code))return ResultMsg.fail("请先获取验证码",null,null);
                if(Tool.isNull(code))return ResultMsg.fail("请输入验证码",null,null);
                if(!session_code.equals(code))return ResultMsg.fail("验证码错误",null,null);
            }
            EntityWrapper<UserApi> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("phone",phone);
            List<UserApi>  userApis = userApiService.selectList(entityWrapper);
            String apiToken=UuidUtil.get32UUID();
            if(!Tool.listIsNull(userApis)){
                UserApi userApi = userApis.get(0);
                Map<String,Object>userApiMap= JavaBeanUtil.convertBeanToMap(userApi);
                UserInfo info=new UserInfo();
                info.setApiToken(apiToken);
                EntityWrapper<UserInfo>userInfoEntityWrapper=new EntityWrapper<>();
                userInfoEntityWrapper.eq("user_id",userApi.getId());
                userInfoService.update(info,userInfoEntityWrapper);
                userApiMap.put("apiToken",apiToken);
                Tool.removeMapParmeByKey(userApiMap,new String[]{"salt","password","object_name","verification_code","account"});
                return ResultMsg.success("登录成功",null,userApiMap);
            }else{
                UserApi userApi=new UserApi();
                userApi.setPhone(phone);
                userApi.setCreatetime(new Date());
                userApi.setSex("0");
                userApi.setName("新用户"+userApiService.selectCount(new EntityWrapper<>()));
                userApi.setAvatar("http://cheshi654321.oss-cn-beijing.aliyuncs.com/data/1539593701464.png?Expires=1854953699&OSSAccessKeyId=LTAIPAfHfcl2Ycjs&Signature=uirIzW4yaf4TjkiD2LA6%2BgjG1Sg%3D");
                userApi.setObject_name("data/1539593701464.png");
                userApiService.insert(userApi);
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(userApi.getId());
                userInfo.setApiToken(apiToken);
                userInfo.setCredits(0);
                userInfo.setMoney(0);
                userInfo.setLoginIp("192.168.0.0.1");
                userInfo.setCreateTime(new DateTime());
                userInfo.setRealName(userApi.getName());
                userInfo.setJoinClub(0);
                userInfo.setAppointment(0);
                userInfo.setEnlightening(0);
                userInfoService.insert(userInfo);
                Map<String,Object>userApiMap= JavaBeanUtil.convertBeanToMap(userApi);
                userApiMap.put("apiToken",apiToken);
                Tool.removeMapParmeByKey(userApiMap,new String[]{"salt","password","object_name","verification_code","account"});
                return ResultMsg.success("登录成功",null,userApiMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultMsg.fail("系统错误",e.toString(),null);
        }
    }



   /* @RequestMapping("realName")
    @ResponseBody
    public ResultMsg realName(String IDcard,String name){

    }*/

}
