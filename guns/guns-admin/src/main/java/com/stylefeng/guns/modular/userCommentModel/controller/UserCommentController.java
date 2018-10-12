package com.stylefeng.guns.modular.userCommentModel.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.service.IMenuService;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.UserComment;
import com.stylefeng.guns.modular.userCommentModel.service.IUserCommentService;

import java.util.List;

/**
 * 评论管理控制器
 *
 * @author fengshuonan
 * @Date 2018-10-12 13:38:01
 */
@Controller
@RequestMapping("/userComment")
public class UserCommentController extends BaseController {

    private String PREFIX = "/userCommentModel/userComment/";

    @Autowired
    private IUserCommentService userCommentService;

    /**
     * 跳转到评论管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userComment.html";
    }

    /**
     * 跳转到添加评论管理
     */
//    @RequestMapping("/userComment_add")
//    public String userCommentAdd() {
//        return PREFIX + "userComment_add.html";
//    }
    @Autowired
    private IUserApiService userApiService;
    /**
     * 跳转到修改评论管理
     */
    @RequestMapping("/userComment_update/{userCommentId}")
    public String userCommentUpdate(@PathVariable Integer userCommentId, Model model) {
        UserComment userComment = userCommentService.selectById(userCommentId);
        UserApi user=new UserApi();
        user.setId(Integer.parseInt(userComment.getUserId()));
        user=userApiService.selectOne(new EntityWrapper<>(user));
        userComment.setUserId(user!=null?user.getName():"<span style='color:red'>用户不存在</span>");
        if(!Tool.isNull(userComment.getCommentId())&&!"0".equals(userComment.getCommentId().trim())){
            UserComment comment=userCommentService.selectById(Integer.parseInt(userComment.getCommentId()));
            userComment.setCommentId(comment!=null?comment.getContent():"<span style='color:red'>被引用的评论已被删除</span>");
        }else{
            userComment.setCommentId("无");
        }
        model.addAttribute("item",userComment);
        LogObjectHolder.me().set(userComment);
        return PREFIX + "userComment_edit.html";
    }

    /**
     * 获取评论管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<UserComment> commentList=userCommentService.selectList(null);
        for (UserComment userComment : commentList) {
            UserApi user=new UserApi();
            user.setId(Integer.parseInt(userComment.getUserId()));
            user=userApiService.selectOne(new EntityWrapper<>(user));
            userComment.setUserId(user!=null?user.getName():"<span style='color:red'>用户不存在</span>");
        }
        return commentList;
    }

    /**
     * 新增评论管理
     */
//    @RequestMapping(value = "/add")
//    @ResponseBody
//    public Object add(UserComment userComment) {
//        userCommentService.insert(userComment);
//        return SUCCESS_TIP;
//    }

    /**
     * 删除评论管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userCommentId) {
        userCommentService.deleteById(userCommentId);
        return SUCCESS_TIP;
    }

    /**
     * 修改评论管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UserComment userComment) {
        userCommentService.updateById(userComment);
        return SUCCESS_TIP;
    }

    /**
     * 评论管理详情
     */
    @RequestMapping(value = "/detail/{userCommentId}")
    @ResponseBody
    public Object detail(@PathVariable("userCommentId") Integer userCommentId) {
        return userCommentService.selectById(userCommentId);
    }
}
