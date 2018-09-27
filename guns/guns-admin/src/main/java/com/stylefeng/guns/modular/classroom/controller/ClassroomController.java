package com.stylefeng.guns.modular.classroom.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.system.warpper.ClassroomWarpper;
import com.stylefeng.guns.modular.system.warpper.UserInfoWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Classroom;
import com.stylefeng.guns.modular.classroom.service.IClassroomService;

import java.util.List;
import java.util.Map;

/**
 * 星厨课堂控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 10:15:18
 */
@Controller
@RequestMapping("/classroom")
public class ClassroomController extends BaseController {

    private String PREFIX = "/classroom/classroom/";

    @Autowired
    private IClassroomService classroomService;

    /**
     * 跳转到星厨课堂首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "classroom.html";
    }

    /**
     * 跳转到添加星厨课堂
     */
    @RequestMapping("/classroom_add")
    public String classroomAdd() {
        return PREFIX + "classroom_add.html";
    }

    /**
     * 跳转到修改星厨课堂
     */
    @RequestMapping("/classroom_update/{classroomId}")
    public String classroomUpdate(@PathVariable Integer classroomId, Model model) {
        Classroom classroom = classroomService.selectById(classroomId);
        model.addAttribute("item",classroom);
        LogObjectHolder.me().set(classroom);
        return PREFIX + "classroom_edit.html";
    }

    /**
     * 获取星厨课堂列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String,Object>> list = classroomService.list(condition);

     return  super.warpObject(new ClassroomWarpper(list));
    }

    /**
     * 新增星厨课堂
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Classroom classroom) {
        classroom.setCreateTime(new DateTime());
        classroomService.insert(classroom);
        return SUCCESS_TIP;
    }

    /**
     * 删除星厨课堂
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer classroomId) {
        classroomService.deleteById(classroomId);
        return SUCCESS_TIP;
    }

    /**
     * 修改星厨课堂
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Classroom classroom) {
        classroom.setUpdateTime(new DateTime());
        classroomService.updateById(classroom);
        return SUCCESS_TIP;
    }

    /**
     * 星厨课堂详情
     */
    @RequestMapping(value = "/detail/{classroomId}")
    @ResponseBody
    public Object detail(@PathVariable("classroomId") Integer classroomId) {
        return classroomService.selectById(classroomId);
    }
}
