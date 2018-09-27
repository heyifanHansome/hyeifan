package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.modular.system.dao.ColumnTypeMapper;
import com.stylefeng.guns.modular.system.dao.GradeMapper;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import com.stylefeng.guns.modular.system.model.Grade;
import com.stylefeng.guns.modular.system.model.User;

import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/9/27.
 */
public class ClubWarpper extends BaseControllerWarpper {
    public ClubWarpper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
        GradeMapper gradeMapper = SpringContextHolder.getBean(GradeMapper.class);

        Integer userId = (Integer) map.get("user_id");
        Integer gradeId = (Integer) map.get("grade_id");
        Integer status = (Integer) map.get("status");
        User user = userMapper.selectById(userId);
        Grade grade  =  gradeMapper.selectById(gradeId);

        if (user != null) {
          map.put("userName",user.getName());
        }else {
            map.put("userName","没有对应的信息");
        }

        if (grade != null) {
            map.put("gradeName",grade.getName());
        }else {
            map.put("gradeName","没有对应的信息");
        }

    if(status!=null){
        if(status==0){
            map.put("status","未审查");
        }
        if(status==1){
            map.put("status","考察");
        }
        if(status==2){
            map.put("status","通过");
        }
        if(status==3){
            map.put("status","通过");
        }
    }else{
            map.put("status","没有相关信息");
    }
    }
}
