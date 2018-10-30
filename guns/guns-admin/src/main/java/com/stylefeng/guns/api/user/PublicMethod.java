package com.stylefeng.guns.api.user;

import com.stylefeng.guns.modular.lijun.util.FSS;
import com.stylefeng.guns.modular.lijun.util.SettingConfiguration;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import com.stylefeng.guns.modular.userTarget.service.IUserTargetService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PublicMethod {
    @Autowired
    public IUserApiService userApiService;
    @Autowired
    public IUserInfoService userInfoService;
    @Autowired
    public SettingConfiguration settingConfiguration;
    @Autowired
    public ITagService tagService;
    @Autowired
    public Dao dao;
    @Autowired
    public IUserTargetService userTargetService;

    /**
     * 根据phone和token获取对应用户的表数据以及绑定的详情表数据
     * @param phone
     * @param token
     * @return
     */
    public List<Map<String, Object>> getUser(String phone, String token){
        return dao.selectBySQL("SELECT api.*,info.id info_id,info.user_id,info.api_token,info.credits,info.money,info.login_ip,info.create_time,info.update_time,info.real_name,info.id_card,info.city_id,info.join_club,info.appointment,info.enlightening FROM "+ FSS.user_api+" api RIGHT JOIN "+FSS.user_info+" info ON (api.id = info.user_id) WHERE api.phone = '"+phone+"' AND info.api_token = '"+token+"'");
    }

    /**
     * 根据base_id去sys_picture表拉取真实图片url集合
     * @param base_id
     * @return
     */
    public List<String> getALiOSSPircturesByBaseId(String base_id){
        List<String>ALiOSSPirctures=new ArrayList<>();
        for (Map<String, Object> stringObjectMap : dao.selectBySQL("select * from "+FSS.picture+" where base_id='"+base_id+"'")) {
            if(!Tool.isNull(stringObjectMap.get("oss_object_name")))ALiOSSPirctures.add(stringObjectMap.get("oss_object_name").toString());
        }
        return ALiOSSPirctures;
    }

    /**
     * 根据栏目ID获取栏目名称
     * @param column_id
     * @return
     */
    public String getColumnNameByColumnId(Object column_id){
        List<Map<String,Object>>columns=dao.selectBySQL("select * from "+FSS.column_type+" where id="+column_id);
        return !Tool.listIsNull(columns)&&!Tool.isNull(columns.get(0).get("name"))?columns.get(0).get("name").toString():"";
    }

    /**
     * 根据城市ID获取城市名称
     * @param city_id
     * @return
     */
    public String getCityNameByCityId(Object city_id){
        List<Map<String,Object>>citys=dao.selectBySQL("select * from "+FSS.city+" where id="+city_id);
        return !Tool.listIsNull(citys)&&!Tool.isNull(citys.get(0).get("name"))?citys.get(0).get("name").toString():"";
    }
}
