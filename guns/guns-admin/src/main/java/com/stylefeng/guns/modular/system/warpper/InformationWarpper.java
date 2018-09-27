package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.modular.system.dao.CityMapper;
import com.stylefeng.guns.modular.system.dao.ColumnTypeMapper;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import com.stylefeng.guns.modular.system.model.City;
import com.stylefeng.guns.modular.system.model.ColumnType;
import com.stylefeng.guns.modular.system.model.User;

import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/9/21.
 */
public class InformationWarpper extends BaseControllerWarpper {
    public InformationWarpper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        ColumnTypeMapper columnTypeMapper = SpringContextHolder.getBean(ColumnTypeMapper.class);

        UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);

        CityMapper cityMapper = SpringContextHolder.getBean(CityMapper.class);
        Integer cityId = (Integer) map.get("city_id");
        Integer userId = (Integer) map.get("uid");
        Integer columnId = (Integer) map.get("column_id");
        Integer sourceId = (Integer) map.get("source_id");
        ColumnType columnType = columnTypeMapper.selectById(columnId);
        User user = userMapper.selectById(userId);
        City city = cityMapper.selectById(cityId);


        if (columnType != null) {
            map.put("columnTypeName", columnType.getName());
        } else {
            map.put("columnTypeName", "没有对应栏目!");
        }

        if (city != null) {
            map.put("cityName", city.getName());
        } else {
            map.put("cityName", "没有对应城市信息!");
        }

        if (user != null) {
            map.put("userName", user.getName());
        } else {
            map.put("userName", "没有对应用户!");
        }

        if (user != null) {
            map.put("userName", user.getName());
        } else {
            map.put("userName", "没有对应用户!");
        }

        if (sourceId != null) {
            if (sourceId == 0) {
                map.put("sourceName", "官方");
            }
            if (sourceId == 1) {
                map.put("sourceName", "个人");
            }

        } else {
            map.put("sourceName", "没有相关信息");
        }


    }
}
