package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;

import java.util.List;
import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/10/11.
 */
public class UserApiWarpper extends BaseControllerWarpper {
    public UserApiWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        Integer sexName  = (Integer) map.get("sex");
        if(sexName==1){
            map.put("sexName","男性");
        }else {
            map.put("sexName","女性");
        }
    }
}
