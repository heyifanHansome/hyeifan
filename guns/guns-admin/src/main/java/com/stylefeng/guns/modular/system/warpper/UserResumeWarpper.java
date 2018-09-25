package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import com.stylefeng.guns.modular.system.model.User;
import io.swagger.models.auth.In;

import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/9/25.
 */
public class UserResumeWarpper extends BaseControllerWarpper {
    /*构造方法初始化参数*/
    public UserResumeWarpper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
         UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
        /*定义需要封装的字段*/
        Integer userId = (Integer) map.get("user_id");
        Integer applyStatus = (Integer) map.get("apply_status");
        Integer sex = (Integer) map.get("sex");

        if (applyStatus != null  && applyStatus==0) {
            map.put("PapplyStatus", "离职-随时到岗");
        }

        if (applyStatus != null &&applyStatus==1) {
            map.put("PapplyStatus", "在职-暂不考虑");
        }

        if (applyStatus != null &&applyStatus==2) {
            map.put("PapplyStatus", "在职-考虑机会");
        }

        if (applyStatus != null &&applyStatus==3) {
            map.put("PapplyStatus", "在职-月内到岗");
        }


        if (sex != null && sex == 1) {
            map.put("Psex", "男");
        } else {
            map.put("Psex", "女");
        }

        User user = userMapper.selectById(userId);
        map.put("Pname",user.getName());

    }
}
