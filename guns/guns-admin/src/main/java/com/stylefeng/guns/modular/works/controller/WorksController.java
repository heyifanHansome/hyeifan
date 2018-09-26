package com.stylefeng.guns.modular.works.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.picture.service.IPictureService;
import com.stylefeng.guns.modular.system.model.Picture;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.Works;
import com.stylefeng.guns.modular.works.service.IWorksService;

import java.util.List;

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

    @Autowired
    private IPictureService pictureService;

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

        works.setCreateTime(new DateTime());
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
        works.setUpdateTime(new DateTime());
        worksService.updateById(works);
        return SUCCESS_TIP;
    }




    @GetMapping(value = "/img")
    @ResponseBody
    public ResponseEntity<?> getUser(String baseId) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            EntityWrapper<Picture> entityWrapper = new EntityWrapper<>();
            entityWrapper.like("base_id",baseId);
            List<Picture> picture = pictureService.selectList(entityWrapper);
            StringBuffer sbBuffer = new StringBuffer();

            for (int i = 0; i < picture.size(); i++) {
                sbBuffer.append("," + picture.get(i).getId());
            }

            resultMsg = ResultMsg.success("查询成功", "查询成功", sbBuffer);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ResultMsg>(ResultMsg.fail("系统错误", "系统错误", ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ResultMsg>(resultMsg, HttpStatus.OK);

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
