package com.stylefeng.guns.modular.city.controller;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.SettingConfiguration;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.warpper.CityWarpper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.City;
import com.stylefeng.guns.modular.city.service.ICityService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 城市管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 15:25:54
 */
@Controller
@RequestMapping("/city")
public class CityController extends BaseController {

    private String PREFIX = "/city/city/";

    @Autowired
    private ICityService cityService;

    /**
     * 跳转到城市管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "city.html";
    }

    /**
     * 跳转到添加城市管理
     */
    @RequestMapping("/city_add")
    public String cityAdd() {
        return PREFIX + "city_add.html";
    }

    /**
     * 跳转到修改城市管理
     */
    @RequestMapping("/city_update/{cityId}")
    public String cityUpdate(@PathVariable Integer cityId, Model model) {
        City city = cityService.selectById(cityId);
        model.addAttribute("item",city);
        LogObjectHolder.me().set(city);
        return PREFIX + "city_edit.html";
    }

    /**
     * 获取城市管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {

        List<Map<String, Object>> list  = cityService.list(condition);
        return super.warpObject(new CityWarpper(list));
    }

    /**
     * 新增城市管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(City city) {

        city.setCreateTime(new DateTime());
        cityService.insert(city);

        return SUCCESS_TIP;
    }

    /**
     * 删除城市管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cityId) {
        cityService.deleteById(cityId);
        return SUCCESS_TIP;
    }

    /**
     * 修改城市管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(City city) {

        city.setUpdateTime(new DateTime());
        cityService.updateById(city);

        return SUCCESS_TIP;
    }

    /**
     * 获取所以城市
     */
    @RequestMapping(value = "/getAllCity")
    @ResponseBody
    public List<City> getAllCity(City city) {
        List<City> citys  = cityService.selectList(null);
        return citys;
    }

    /**
     * 城市管理详情
     */
    @RequestMapping(value = "/detail/{cityId}")
    @ResponseBody
    public Object detail(@PathVariable("cityId") Integer cityId) {
        return cityService.selectById(cityId);
    }
@Autowired
private SettingConfiguration settingConfiguration;
    @RequestMapping("dingwei")
    @ResponseBody
    public Object dingwei(String cityName){
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建http请求(get方式)
        Setting setting=settingConfiguration.getSetting();
        HttpGet httpGet = new HttpGet("http://restapi.amap.com/v3/geocode/geo?parameters&key="+setting.getGd_key()+"&address="+cityName);
        httpGet.setHeader("Content-Type", "application/json-rpc");
        // 发送请求并接收结果（这里已经接收了到了返回结果，后面的代码只做了一些转换）
        try{
            HttpResponse httpResponse = client.execute(httpGet);
            String responseBody = EntityUtils.toString(httpResponse.getEntity());
            JSONObject object=JSONObject.fromObject(responseBody);
            if(object.containsKey("status")){
                if("1".equals(object.get("status"))){
                    if(object.containsKey("geocodes")){
                        JSONArray geocodes=JSONArray.fromObject(object.get("geocodes"));
                        System.err.println(geocodes);
                        System.err.println(Tool.listIsNull(geocodes));
                        if(!Tool.listIsNull(geocodes)){
                            return ResultMsg.success("定位成功!",null,object);
                        }else{
                            return ResultMsg.fail("定位失败!城市名称不够详细!",null,object);
                        }
                    }else{
                        return ResultMsg.fail("定位失败!",null,object);
                    }
                }else{
                    return ResultMsg.fail("定位失败!",null,object);
                }
            }else{
                return ResultMsg.fail("定位失败!",null,object);
            }
        }catch (IOException e){
            e.printStackTrace();
            return ResultMsg.fail("定位失败!系统异常!",null,e);
        }
    }
}
