package com.stylefeng.guns.api.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.model.UserInfo;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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

    /**
     * 注册用户发送验证码
     * @param phone 前台传入的手机号
     * @return  发送成功或者失败的信息
     */
    @RequestMapping("/getVerificationCode")
    @ResponseBody
    public ResultMsg getVerificationCode(String phone){
        ResultMsg resultMsg = new ResultMsg();
        UserApi userApi = new UserApi();
        try {
            userApi.setPhone(phone);
            userApi.setVerificationCode(ShiroKit.getRandomSalt(6));
            userApiService.insert(userApi);
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultMsg.fail("发送验证码失败!","code : 500",HttpStatus.BAD_REQUEST);
        }
    return ResultMsg.success("发送验证码成功!","code : 0 "  ,HttpStatus.OK);

    }

    /**
     * 根据用户手机号和验证码登录并且记录用户信息
     * @param phone 手机号
     * @param code 验证码
     * @return
     */
    @RequestMapping("/userLogin")
    @ResponseBody
    public ResultMsg userLogin(String phone,String code){
        try {
            EntityWrapper<UserApi> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("phone",phone);
            List<UserApi>  userApis = userApiService.selectList(entityWrapper);
            UserApi userApi = userApis.get(0);
            if (userApis.size()> 0) {
                    return   ResultMsg.fail("手机号输入错误,请重新输入!","code : 404",HttpStatus.BAD_REQUEST);
            }else {
            if(userApi.getVerificationCode() == code){
                userApi.setCreatetime( new DateTime());
                userApiService.updateById(userApi);
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(userApi.getId());
                userInfo.setApiToken(ShiroKit.getRandomSalt(5));
                userInfo.setCredits(0);
                userInfo.setMoney(0);
                userInfo.setLoginIp("192.168.0.0.1");
                userInfo.setCreateTime(new DateTime());
                userInfo.setRealName(userApi.getName());
                userInfo.setJoinClub(0);
                userInfo.setAppointment(0);
                userInfo.setEnlightening(0);
                userInfoService.insert(userInfo);
            return   ResultMsg.success("登录成功!","code : 404",HttpStatus.BAD_REQUEST);
            }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return  ResultMsg.fail("服务器响应错误!","code : 500",HttpStatus.BAD_REQUEST);
        }
        return   ResultMsg.success("登录成功!","code : 404",HttpStatus.BAD_REQUEST);
    }











}
