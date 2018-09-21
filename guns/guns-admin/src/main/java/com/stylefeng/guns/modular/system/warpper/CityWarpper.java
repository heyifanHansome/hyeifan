package com.stylefeng.guns.modular.system.warpper;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.dao.CityMapper;
import com.stylefeng.guns.modular.system.dao.CityTypeMapper;
import com.stylefeng.guns.modular.system.model.CityType;

import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/9/21.
 */
public class CityWarpper extends BaseControllerWarpper {
    public CityWarpper(Object list) {
        super(list);
    }
    @Override
    protected void warpTheMap(Map<String, Object> map) {
        CityTypeMapper cityTypeMapper = SpringContextHolder.getBean(CityTypeMapper.class);

        Integer cityTypeId =   (Integer) map.get("type_id");

        CityType cityType = cityTypeMapper.selectById(cityTypeId);
        if (cityType!= null) {
            map.put("cityTypeName", cityType.getName());
        } else {
            map.put("cityTypeName", "大兄弟你在干什么!");
        }

    }
}
