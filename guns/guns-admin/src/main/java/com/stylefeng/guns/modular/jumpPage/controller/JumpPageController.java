package com.stylefeng.guns.modular.jumpPage.controller;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.modular.lijun.util.SettingConfiguration;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.Setting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.JumpPage;
import com.stylefeng.guns.modular.jumpPage.service.IJumpPageService;

import java.util.List;

/**
 * 首页跳转模块控制器
 *
 * @author fengshuonan
 * @Date 2018-10-23 09:54:46
 */
@Controller
@RequestMapping("/jumpPage")
public class JumpPageController extends BaseController {

    private String PREFIX = "/jumpPage/jumpPage/";

    @Autowired
    private SettingConfiguration settingConfiguration;
    @Autowired
    private IJumpPageService jumpPageService;

    /**
     * 跳转到首页跳转模块首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "jumpPage.html";
    }

    /**
     * 跳转到添加首页跳转模块
     */
    @RequestMapping("/jumpPage_add")
    public String jumpPageAdd() {
        return PREFIX + "jumpPage_add.html";
    }

    /**
     * 跳转到修改首页跳转模块
     */
    @RequestMapping("/jumpPage_update/{jumpPageId}")
    public String jumpPageUpdate(@PathVariable Integer jumpPageId, Model model) {
        JumpPage jumpPage = jumpPageService.selectById(jumpPageId);
        model.addAttribute("item",jumpPage);
        LogObjectHolder.me().set(jumpPage);
        return PREFIX + "jumpPage_edit.html";
    }

    /**
     * 获取首页跳转模块列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
//        List<JumpPage>jumpPages=jumpPageService.selectList(null);
        List<JumpPage>jumpPages=jumpPageService.selectList(new EntityWrapper<>(new JumpPage()).orderBy("orders",true));
        for (JumpPage jumpPage : jumpPages) {
            switch (jumpPage.getCode()){
                case "0":jumpPage.setCode("星厨俱乐部");
                    break;
                case "1":jumpPage.setCode("拜师学艺");
                    break;
                case "2":jumpPage.setCode("厨友圈");
                    break;
                case "3":jumpPage.setCode("赛事活动");
                    break;
                case "4":jumpPage.setCode("宴席预定");
                    break;
                case "5":jumpPage.setCode("星厨招聘");
                    break;
                default:jumpPage.setCode("<span style='color:red;'>未知板块,请联系技术支持添加</span>");
            }
            switch (jumpPage.getEnable()){
                case "1":jumpPage.setEnable("<span style='color:green;'>是</span>");
                    break;
                default:jumpPage.setEnable("<span style='color:red;'>否</span>");
            }
        }
        return jumpPages;
    }

    /**
     * 新增首页跳转模块
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JumpPage jumpPage,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            Setting setting=settingConfiguration.getSetting();
            OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
            ossClient.deleteObject(setting.getAliOssBucket(), old_object_name);
            ossClient.shutdown();
        }
        if(jumpPageService.selectCount(new EntityWrapper<>(new JumpPage()).eq("code",jumpPage.getCode()))>0)return new ErrorTip(500,"添加失败,您选择的跳转模块已存在");
        jumpPageService.insert(jumpPage);
        return SUCCESS_TIP;
    }

    /**
     * 删除首页跳转模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer jumpPageId) {
        JumpPage jumpPage=jumpPageService.selectOne(new EntityWrapper<>(new JumpPage()).eq("id",jumpPageId));
        Setting setting=settingConfiguration.getSetting();
        OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
        if(!Tool.isNull(jumpPage.getObject_name()))ossClient.deleteObject(setting.getAliOssBucket(),jumpPage.getObject_name());
        ossClient.shutdown();
        jumpPageService.deleteById(jumpPageId);
        return SUCCESS_TIP;
    }

    /**
     * 修改首页跳转模块
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JumpPage jumpPage,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            Setting setting=settingConfiguration.getSetting();
            OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
            ossClient.deleteObject(setting.getAliOssBucket(), old_object_name);
            ossClient.shutdown();
        }
        if(jumpPageService.selectCount(new EntityWrapper<>(new JumpPage()).eq("code",jumpPage.getCode()).ne("id",jumpPage.getId()))>0)return new ErrorTip(500,"修改失败,您选择的跳转模块已存在");
        jumpPageService.updateById(jumpPage);
        return SUCCESS_TIP;
    }

    /**
     * 首页跳转模块详情
     */
    @RequestMapping(value = "/detail/{jumpPageId}")
    @ResponseBody
    public Object detail(@PathVariable("jumpPageId") Integer jumpPageId) {
        return jumpPageService.selectById(jumpPageId);
    }
}
