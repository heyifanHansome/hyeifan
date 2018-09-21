package com.stylefeng.guns.modular.userCollection.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.UserCollection;
import com.stylefeng.guns.modular.userCollection.service.IUserCollectionService;

/**
 * 用户收藏管理控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 16:38:36
 */
@Controller
@RequestMapping("/userCollection")
public class UserCollectionController extends BaseController {

    private String PREFIX = "/userCollection/userCollection/";

    @Autowired
    private IUserCollectionService userCollectionService;

    /**
     * 跳转到用户收藏管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userCollection.html";
    }

    /**
     * 跳转到添加用户收藏管理
     */
    @RequestMapping("/userCollection_add")
    public String userCollectionAdd() {
        return PREFIX + "userCollection_add.html";
    }

    /**
     * 跳转到修改用户收藏管理
     */
    @RequestMapping("/userCollection_update/{userCollectionId}")
    public String userCollectionUpdate(@PathVariable Integer userCollectionId, Model model) {
        UserCollection userCollection = userCollectionService.selectById(userCollectionId);
        model.addAttribute("item",userCollection);
        LogObjectHolder.me().set(userCollection);
        return PREFIX + "userCollection_edit.html";
    }

    /**
     * 获取用户收藏管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return userCollectionService.selectList(null);
    }

    /**
     * 新增用户收藏管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UserCollection userCollection) {
        userCollectionService.insert(userCollection);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户收藏管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userCollectionId) {
        userCollectionService.deleteById(userCollectionId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户收藏管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UserCollection userCollection) {
        userCollectionService.updateById(userCollection);
        return SUCCESS_TIP;
    }

    /**
     * 用户收藏管理详情
     */
    @RequestMapping(value = "/detail/{userCollectionId}")
    @ResponseBody
    public Object detail(@PathVariable("userCollectionId") Integer userCollectionId) {
        return userCollectionService.selectById(userCollectionId);
    }
}
