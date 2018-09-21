package com.stylefeng.guns.modular.resumeFJ.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.ResumeFj;
import com.stylefeng.guns.modular.resumeFJ.service.IResumeFjService;

/**
 * 简历附件控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 11:40:13
 */
@Controller
@RequestMapping("/resumeFj")
public class ResumeFjController extends BaseController {

    private String PREFIX = "/resumeFJ/resumeFj/";

    @Autowired
    private IResumeFjService resumeFjService;

    /**
     * 跳转到简历附件首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "resumeFj.html";
    }

    /**
     * 跳转到添加简历附件
     */
    @RequestMapping("/resumeFj_add")
    public String resumeFjAdd() {
        return PREFIX + "resumeFj_add.html";
    }

    /**
     * 跳转到修改简历附件
     */
    @RequestMapping("/resumeFj_update/{resumeFjId}")
    public String resumeFjUpdate(@PathVariable Integer resumeFjId, Model model) {
        ResumeFj resumeFj = resumeFjService.selectById(resumeFjId);
        model.addAttribute("item",resumeFj);
        LogObjectHolder.me().set(resumeFj);
        return PREFIX + "resumeFj_edit.html";
    }

    /**
     * 获取简历附件列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return resumeFjService.selectList(null);
    }

    /**
     * 新增简历附件
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ResumeFj resumeFj) {
        resumeFjService.insert(resumeFj);
        return SUCCESS_TIP;
    }

    /**
     * 删除简历附件
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer resumeFjId) {
        resumeFjService.deleteById(resumeFjId);
        return SUCCESS_TIP;
    }

    /**
     * 修改简历附件
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ResumeFj resumeFj) {
        resumeFjService.updateById(resumeFj);
        return SUCCESS_TIP;
    }

    /**
     * 简历附件详情
     */
    @RequestMapping(value = "/detail/{resumeFjId}")
    @ResponseBody
    public Object detail(@PathVariable("resumeFjId") Integer resumeFjId) {
        return resumeFjService.selectById(resumeFjId);
    }
}
