package com.stylefeng.guns.modular.infomation.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.system.model.ColumnType;
import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.warpper.InformationWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Information;
import com.stylefeng.guns.modular.infomation.service.IInformationService;

import java.util.List;
import java.util.Map;

/**
 * 资讯管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-21 10:41:38
 */
@Controller
@RequestMapping("/information")
public class InformationController extends BaseController {

    private String PREFIX = "/infomation/information/";

    @Autowired
    private IInformationService informationService;

    @Autowired
    private IColumnTypeService columnTypeService;
    /**
     * 跳转到资讯管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "information.html";
    }

    /**
     * 跳转到添加资讯管理
     */
    @RequestMapping("/information_add")
    public String informationAdd() {
        return PREFIX + "information_add.html";
    }

    /**
     * 跳转到修改资讯管理
     */
    @RequestMapping("/information_update/{informationId}")
    public String informationUpdate(@PathVariable Integer informationId, Model model) {
        Information information = informationService.selectById(informationId);
        model.addAttribute("item",information);
        LogObjectHolder.me().set(information);
        return PREFIX + "information_edit.html";
    }

    /**
     * 获取资讯管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {

        List<Map<String,Object>> list = informationService.list(condition);
        return  super.warpObject(new InformationWarpper(list));
    }

    /**
     * 新增资讯管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Information information) {
        information.setCreateTime(new DateTime());
        informationService.insert(information);
        return SUCCESS_TIP;
    }

    /**
     * 删除资讯管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer informationId) {
        informationService.deleteById(informationId);
        return SUCCESS_TIP;
    }

    /**
     * 修改资讯管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Information information) {
        information.setUpdateTime(new DateTime());
        informationService.updateById(information);
        return SUCCESS_TIP;
    }

    /**
     * 资讯管理详情
     */
    @RequestMapping(value = "/detail/{informationId}")
    @ResponseBody
    public Object detail(@PathVariable("informationId") Integer informationId) {
        return informationService.selectById(informationId);
    }

    /**
     * 获取栏目类型
     */
    @RequestMapping(value = "/getColumnTypeInformation")
    @ResponseBody
    public Object getColumnTypeInformation() {
        EntityWrapper<ColumnType> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("menu_id ={0}","1049863045267951793");
        List<ColumnType> columnTypes = columnTypeService.selectList(entityWrapper);
        return  columnTypes;
    }

    /**
     * 获取标签数据
     */
    @RequestMapping(value = "/getTagValues")
    @ResponseBody
    public Object getTagValues() {
        EntityWrapper<Tag> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("","1049863045267951793");

        return  "";
    }




}
