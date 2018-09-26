package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.modular.system.dao.RoleMapper;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import com.stylefeng.guns.modular.system.dao.WorksMapper;
import com.stylefeng.guns.modular.system.model.Role;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.model.Works;
import io.swagger.models.auth.In;

import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/9/25.
 */
public class UserWorksWarpper extends BaseControllerWarpper {
    /*构造方法初始化参数*/
    public UserWorksWarpper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        WorksMapper worksMapper = SpringContextHolder.getBean(WorksMapper.class);
        UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
        RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
        /*定义需要封装的字段*/
        Integer userId = (Integer) map.get("user_id");
        Integer roleId = (Integer) map.get("role_id");
        Integer worksId =(Integer) map.get("works_id");
        User user = userMapper.selectById(userId);
        Works works = worksMapper.selectById(worksId);
        Role role = roleMapper.selectById(roleId);
        map.put("Pname",user.getName());
        map.put("Prole",role.getName());
        map.put("Pworks",works.getName());

    }
}
