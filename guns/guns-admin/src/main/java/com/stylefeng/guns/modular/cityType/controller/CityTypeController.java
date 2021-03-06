package com.stylefeng.guns.modular.cityType.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.CityType;
import com.stylefeng.guns.modular.cityType.service.ICityTypeService;

import java.util.List;

/**
 * 城市分类控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 15:28:50
 */
@Controller
@RequestMapping("/cityType")
public class CityTypeController extends BaseController {

    private String PREFIX = "/cityType/cityType/";

    @Autowired
    private ICityTypeService cityTypeService;

    /**
     * 跳转到城市分类首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "cityType.html";
    }

    /**
     * 跳转到添加城市分类
     */
    @RequestMapping("/cityType_add")
    public String cityTypeAdd() {
        return PREFIX + "cityType_add.html";
    }

    /**
     * 跳转到修改城市分类
     */
    @RequestMapping("/cityType_update/{cityTypeId}")
    public String cityTypeUpdate(@PathVariable Integer cityTypeId, Model model) {
        CityType cityType = cityTypeService.selectById(cityTypeId);
        model.addAttribute("item", cityType);
        LogObjectHolder.me().set(cityType);
        return PREFIX + "cityType_edit.html";
    }

    /**
     * 获取城市分类列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return cityTypeService.selectList(null);
    }

    /**
     * 新增城市分类
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CityType cityType) {
        cityType.setCreateTime(new DateTime());
        cityTypeService.insert(cityType);

        return SUCCESS_TIP;
    }

    /**
     * 删除城市分类
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer cityTypeId) {
        cityTypeService.deleteById(cityTypeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改城市分类
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CityType cityType) {
        cityType.setUpdatedTime(new DateTime());
        cityTypeService.updateById(cityType);

        return SUCCESS_TIP;
    }


    /**
     * 获取全部城市分类
     * @return
     */
    @RequestMapping(value = "getAllCityType")
    @ResponseBody
    public List<CityType> getAllCityType() {
        List<CityType> cityTypes = cityTypeService.selectList(null);
        return cityTypes;
    }


    /**
     * 城市分类详情
     */
    @RequestMapping(value = "/detail/{cityTypeId}")
    @ResponseBody
    public Object detail(@PathVariable("cityTypeId") Integer cityTypeId) {
        return cityTypeService.selectById(cityTypeId);
    }
}
