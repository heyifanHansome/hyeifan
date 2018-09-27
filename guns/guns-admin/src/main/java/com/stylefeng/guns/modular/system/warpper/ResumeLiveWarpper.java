package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import com.stylefeng.guns.modular.system.dao.UserResumeMapper;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.model.UserResume;

import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/9/25.
 */
public class ResumeLiveWarpper extends BaseControllerWarpper {
    /*构造方法初始化参数*/
    public ResumeLiveWarpper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
         UserResumeMapper userResumeMapper = SpringContextHolder.getBean(UserResumeMapper.class);
            Integer usresumeId = (Integer) map.get("resume_id");
            UserResume userResume = userResumeMapper.selectById(usresumeId);
        if (userResume != null) {
            map.put("userSumerName",userResume.getName());
        }else {
            map.put("userSumerName","没有对应简历");
        }
    }
}
