package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import com.stylefeng.guns.modular.system.model.User;

import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/9/25.
 */
public class UserInfoWarpper extends BaseControllerWarpper {
    /*构造方法初始化参数*/
    public UserInfoWarpper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
        /*定义需要封装的字段*/
        Integer userId = (Integer) map.get("user_id");
        Integer credits = (Integer) map.get("credits");
        Integer money = (Integer) map.get("money");
        Integer joinClub = (Integer) map.get("joinClub");
        Integer appointment = (Integer) map.get("appointment");
        Integer enlightening = (Integer) map.get("enlightening");
        String loginIp = (String) map.get("api_token");
        String apiToken = (String) map.get("api_token");

        if (loginIp != null) {
            map.put("PloginIp", loginIp);
        } else {
            map.put("PloginIp", "未获取");
        }

        if (apiToken != null) {
            map.put("PapiToken", apiToken);
        } else {
            map.put("PapiToken", "未获取");
        }

        if (credits != null) {
            String creditName = credits + "RMB";
            map.put("Pcredits", credits);
        } else {
            map.put("Pcredits", "当前用户没有积分");
        }


        if (money != null) {
            String moneyName = money + "RMB";
            map.put("Pmoney", moneyName);
        } else {
            map.put("Pmoney", "当前用户余额为0RMB");
        }

        if (joinClub != null && joinClub == 1) {
            map.put("PjoinClub", "已加入");
        } else {
            map.put("PjoinClub", "未加入");
        }

        if (appointment != null && appointment == 1) {
            map.put("Pappointment", "已预约");
        } else {
            map.put("Pappointment", "未预约");
        }

        if (enlightening != null && enlightening == 1) {
            map.put("Penlightening", "已开启");
        } else {
            map.put("Penlightening", "未开启");
        }
        User user = userMapper.selectById(userId);
        if (user != null) {
            map.put("Pname", user.getName());
        } else {
            map.put("Pname", "没有相关信息");

        }

    }
}
