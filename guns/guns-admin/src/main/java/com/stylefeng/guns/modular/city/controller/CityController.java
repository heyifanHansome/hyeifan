package com.stylefeng.guns.modular.city.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.system.warpper.CityWarpper;
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
}
