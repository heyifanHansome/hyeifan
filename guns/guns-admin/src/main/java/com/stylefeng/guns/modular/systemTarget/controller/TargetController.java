package com.stylefeng.guns.modular.systemTarget.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.lijun.util.JavaBeanUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Target;
import com.stylefeng.guns.modular.systemTarget.service.ITargetService;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 会员等级评价指标控制器
 *
 * @author fengshuonan
 * @Date 2018-10-16 12:59:25
 */
@Controller
@RequestMapping("/target")
public class TargetController extends BaseController {

    private String PREFIX = "/systemTarget/target/";

    @Autowired
    private ITargetService targetService;

    /**
     * 跳转到会员等级评价指标首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "target.html";
    }

    /**
     * 跳转到添加会员等级评价指标
     */
    @RequestMapping("/target_add")
    public String targetAdd() {
        return PREFIX + "target_add.html";
    }

    /**
     * 跳转到修改会员等级评价指标
     */
    @RequestMapping("/target_update/{targetId}")
    public String targetUpdate(@PathVariable Integer targetId, Model model) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Target target = targetService.selectById(targetId);
        Map<String,Object>targetMap= JavaBeanUtil.convertBeanToMap(target);
        if("0".equals(targetMap.get("pid"))){
            targetMap.put("pidName","顶级");
        }else{
            target=targetService.selectById(target.getPid());
            targetMap.put("pidName",target!=null?target.getName():"不存在,请重新指定");
        }
        model.addAttribute("item",targetMap);
        LogObjectHolder.me().set(targetMap);
        return PREFIX + "target_edit.html";
    }

    /**
     * 获取会员等级评价指标列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return targetService.selectList(null);
    }

    /**
     * 新增会员等级评价指标
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Target target) {
        targetService.insert(target);
        return SUCCESS_TIP;
    }

    /**
     * 删除会员等级评价指标
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer targetId) {
        targetService.deleteById(targetId);
        return SUCCESS_TIP;
    }

    /**
     * 修改会员等级评价指标
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Target target) {
        targetService.updateById(target);
        return SUCCESS_TIP;
    }

    /**
     * 会员等级评价指标详情
     */
    @RequestMapping(value = "/detail/{targetId}")
    @ResponseBody
    public Object detail(@PathVariable("targetId") Integer targetId) {
        return targetService.selectById(targetId);
    }

    /**
     * 获取菜单列表(选择父级菜单用)
     */
    @RequestMapping(value = "/selectTargetTreeList")
    @ResponseBody
    public List<ZTreeNode> selectMenuTreeList() {
        List<ZTreeNode> roleTreeList = this.targetService.targetTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }
}
