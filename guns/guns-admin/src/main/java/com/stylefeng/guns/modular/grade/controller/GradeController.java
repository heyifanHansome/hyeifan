package com.stylefeng.guns.modular.grade.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import com.stylefeng.guns.modular.system.model.Grade;
import com.stylefeng.guns.modular.grade.service.IGradeService;

import java.util.List;

/**
 * 厨师等级表控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 10:24:35
 */
@Controller
@RequestMapping("/grade")
public class GradeController extends BaseController {

    private String PREFIX = "/grade/grade/";

    @Autowired
    private IGradeService gradeService;

    /**
     * 跳转到厨师等级表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "grade.html";
    }

    /**
     * 跳转到添加厨师等级表
     */
    @RequestMapping("/grade_add")
    public String gradeAdd() {
        return PREFIX + "grade_add.html";
    }

    /**
     * 跳转到修改厨师等级表
     */
    @RequestMapping("/grade_update/{gradeId}")
    public String gradeUpdate(@PathVariable Integer gradeId, Model model) {
        Grade grade = gradeService.selectById(gradeId);
        model.addAttribute("item",grade);
        LogObjectHolder.me().set(grade);
        return PREFIX + "grade_edit.html";
    }

    /**
     * 获取厨师等级表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public   List<Grade> list(String condition) {

        EntityWrapper<Grade> entityWrapper = new EntityWrapper<>();
        entityWrapper.like("name",condition);
         List<Grade>  grades = gradeService.selectList(entityWrapper);
         return  grades;
    }

    /**
     * 新增厨师等级表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Grade grade) {
        grade.setCreateTime(new DateTime());
        gradeService.insert(grade);
        return SUCCESS_TIP;
    }

    /**
     * 删除厨师等级表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer gradeId) {
        gradeService.deleteById(gradeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改厨师等级表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Grade grade) {
        grade.setUpdateTime(new DateTime());
        gradeService.updateById(grade);
        return SUCCESS_TIP;
    }

    /**
     * 厨师等级表详情
     */
    @RequestMapping(value = "/detail/{gradeId}")
    @ResponseBody
    public Object detail(@PathVariable("gradeId") Integer gradeId) {
        return gradeService.selectById(gradeId);
    }
}
