package com.stylefeng.guns.modular.club.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Club;
import com.stylefeng.guns.modular.club.service.IClubService;

/**
 * 星厨俱乐部控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 10:40:12
 */
@Controller
@RequestMapping("/club")
public class ClubController extends BaseController {

    private String PREFIX = "/club/club/";

    @Autowired
    private IClubService clubService;

    /**
     * 跳转到星厨俱乐部首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "club.html";
    }

    /**
     * 跳转到添加星厨俱乐部
     */
    @RequestMapping("/club_add")
    public String clubAdd() {
        return PREFIX + "club_add.html";
    }

    /**
     * 跳转到修改星厨俱乐部
     */
    @RequestMapping("/club_update/{clubId}")
    public String clubUpdate(@PathVariable Integer clubId, Model model) {
        Club club = clubService.selectById(clubId);
        model.addAttribute("item",club);
        LogObjectHolder.me().set(club);
        return PREFIX + "club_edit.html";
    }

    /**
     * 获取星厨俱乐部列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return clubService.selectList(null);
    }

    /**
     * 新增星厨俱乐部
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Club club) {
        clubService.insert(club);
        return SUCCESS_TIP;
    }

    /**
     * 删除星厨俱乐部
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer clubId) {
        clubService.deleteById(clubId);
        return SUCCESS_TIP;
    }

    /**
     * 修改星厨俱乐部
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Club club) {
        clubService.updateById(club);
        return SUCCESS_TIP;
    }

    /**
     * 星厨俱乐部详情
     */
    @RequestMapping(value = "/detail/{clubId}")
    @ResponseBody
    public Object detail(@PathVariable("clubId") Integer clubId) {
        return clubService.selectById(clubId);
    }
}
