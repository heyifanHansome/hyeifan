package com.stylefeng.guns.modular.indexUpGroupUser.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.modular.city.service.ICityService;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.City;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.IndexUpGroupUser;
import com.stylefeng.guns.modular.indexUpGroupUser.service.IIndexUpGroupUserService;

import java.util.Date;
import java.util.List;

/**
 * 厨师推荐分组管理控制器
 *
 * @author fengshuonan
 * @Date 2018-10-24 13:45:42
 */
@Controller
@RequestMapping("/indexUpGroupUser")
public class IndexUpGroupUserController extends BaseController {

    private String PREFIX = "/indexUpGroupUser/indexUpGroupUser/";

    @Autowired
    private IIndexUpGroupUserService indexUpGroupUserService;
    @Autowired
    private ICityService cityService;
    @Autowired
    private IUserApiService userApiService;
    /**
     * 跳转到厨师推荐分组管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "indexUpGroupUser.html";
    }

    /**
     * 跳转到添加厨师推荐分组管理
     */
    @RequestMapping("/indexUpGroupUser_add")
    public String indexUpGroupUserAdd() {
        return PREFIX + "indexUpGroupUser_add.html";
    }

    /**
     * 跳转到修改厨师推荐分组管理
     */
    @RequestMapping("/indexUpGroupUser_update/{indexUpGroupUserId}")
    public String indexUpGroupUserUpdate(@PathVariable Integer indexUpGroupUserId, Model model) {
        IndexUpGroupUser indexUpGroupUser = indexUpGroupUserService.selectById(indexUpGroupUserId);
        model.addAttribute("item",indexUpGroupUser);
        LogObjectHolder.me().set(indexUpGroupUser);
        return PREFIX + "indexUpGroupUser_edit.html";
    }

    /**
     * 获取厨师推荐分组管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<IndexUpGroupUser>groupUsers=indexUpGroupUserService.selectList(null);
        for (IndexUpGroupUser groupUser : groupUsers) {
            City city=cityService.selectById(groupUser.getCityId());
            groupUser.setCityId(city!=null?city.getName():"0".equals(groupUser.getCityId())?"全国":"<span style='color:red;'>城市被删除</span>");
            groupUser.setUserApiId(!Tool.isNull(groupUser.getUserApiId())?String.valueOf(groupUser.getUserApiId().split(",").length):"0");
        }
        return groupUsers;
    }

    /**
     * 新增厨师推荐分组管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(IndexUpGroupUser indexUpGroupUser) {
        if(indexUpGroupUserService.selectCount(new EntityWrapper<>(new IndexUpGroupUser()).eq("city_id",indexUpGroupUser.getCityId()))>0)return new ErrorTip(500,"添加失败,您选择城市分组已存在");
        indexUpGroupUser.setSubmitTime(new Date());
        indexUpGroupUserService.insert(indexUpGroupUser);
        return SUCCESS_TIP;
    }

    /**
     * 删除厨师推荐分组管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer indexUpGroupUserId) {
        indexUpGroupUserService.deleteById(indexUpGroupUserId);
        return SUCCESS_TIP;
    }

    /**
     * 修改厨师推荐分组管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(IndexUpGroupUser indexUpGroupUser) {
        if(indexUpGroupUserService.selectCount(new EntityWrapper<>(new IndexUpGroupUser()).eq("city_id",indexUpGroupUser.getCityId()).ne("id",indexUpGroupUser.getId()))>0)return new ErrorTip(500,"修改失败,您选择城市分组已存在");
        indexUpGroupUser.setSubmitTime(new Date());
        indexUpGroupUserService.updateById(indexUpGroupUser);
        return SUCCESS_TIP;
    }

    /**
     * 厨师推荐分组管理详情
     */
    @RequestMapping(value = "/detail/{indexUpGroupUserId}")
    @ResponseBody
    public Object detail(@PathVariable("indexUpGroupUserId") Integer indexUpGroupUserId) {
        return indexUpGroupUserService.selectById(indexUpGroupUserId);
    }

    @RequestMapping("cityList")
    @ResponseBody
    public Object cityList(){
        return cityService.list(null);
    }

    @RequestMapping("userList")
    @ResponseBody
    public Object usersByUserApiIds(){
        return userApiService.list(null);
    }
}
