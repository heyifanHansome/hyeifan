package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.modular.system.dao.ColumnTypeMapper;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import com.stylefeng.guns.modular.system.model.ColumnType;
import com.stylefeng.guns.modular.system.model.User;

import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/9/25.
 */
public class WorksWarpper extends BaseControllerWarpper {
    /*构造方法初始化参数*/
    public WorksWarpper(Object list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        ColumnTypeMapper columnTypeMapper = SpringContextHolder.getBean(ColumnTypeMapper.class);

        /*定义需要封装的字段*/
        Integer columnId = (Integer) map.get("column_id");
        String status = (String) map.get("status");
        String type = (String) map.get("type");
        ColumnType columnType = columnTypeMapper.selectById(columnId);
        if(columnType!=null){
            map.put("columnTypeName",columnType.getName());
        }else {
            map.put("columnTypeName","没有选择栏目");
        }

        if(status!=null){
            if(status.equals("0")){
                map.put("status","未审查");
            }
            if(status.equals("1")){
                map.put("status","通过");
            }
            if(status.equals("2")){
                map.put("status","拒绝");
            }
        }else{
            map.put("status","请输入状态");
        }

        if(type!=null){
            if(type.equals("0")){
                map.put("type","图文");
            }
            if(type.equals("1")){
                map.put("type","视频");
            }
        }else{
            map.put("type","活动");
        }


    }
}
