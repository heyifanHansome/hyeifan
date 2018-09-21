package com.stylefeng.guns.modular.works.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Works;
import com.stylefeng.guns.modular.works.service.IWorksService;

/**
 * 作品管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 10:44:21
 */
@Controller
@RequestMapping("/works")
public class WorksController extends BaseController {
    /*FuckingCrazying*/
    private String PREFIX = "/works/works/";

    @Autowired
    private IWorksService worksService;

    /**
     * 跳转到作品管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "works.html";
    }

    /**
     * 跳转到添加作品管理
     */
    @RequestMapping("/works_add")
    public String worksAdd() {
        return PREFIX + "works_add.html";
    }

    /**
     * 跳转到修改作品管理
     */
    @RequestMapping("/works_update/{worksId}")
    public String worksUpdate(@PathVariable Integer worksId, Model model) {
        Works works = worksService.selectById(worksId);
        model.addAttribute("item",works);
        LogObjectHolder.me().set(works);
        return PREFIX + "works_edit.html";
    }

    /**
     * 获取作品管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return worksService.selectList(null);
    }

    /**
     * 新增作品管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Works works) {
        worksService.insert(works);
        return SUCCESS_TIP;
    }

    /**
     * 删除作品管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer worksId) {
        worksService.deleteById(worksId);
        return SUCCESS_TIP;
    }

    /**
     * 修改作品管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Works works) {
        worksService.updateById(works);
        return SUCCESS_TIP;
    }

    /**
     * 作品管理详情
     */
    @RequestMapping(value = "/detail/{worksId}")
    @ResponseBody
    public Object detail(@PathVariable("worksId") Integer worksId) {
        return worksService.selectById(worksId);
    }
}
