package com.stylefeng.guns.modular.searchKeyword.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.SearchKeyword;
import com.stylefeng.guns.modular.searchKeyword.service.ISearchKeywordService;

/**
 * 搜索关键字管理控制器
 *
 * @author fengshuonan
 * @Date 2018-10-30 13:23:13
 */
@Controller
@RequestMapping("/searchKeyword")
public class SearchKeywordController extends BaseController {

    private String PREFIX = "/searchKeyword/searchKeyword/";

    @Autowired
    private ISearchKeywordService searchKeywordService;

    /**
     * 跳转到搜索关键字管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "searchKeyword.html";
    }

    /**
     * 跳转到添加搜索关键字管理
     */
    @RequestMapping("/searchKeyword_add")
    public String searchKeywordAdd() {
        return PREFIX + "searchKeyword_add.html";
    }

    /**
     * 跳转到修改搜索关键字管理
     */
    @RequestMapping("/searchKeyword_update/{searchKeywordId}")
    public String searchKeywordUpdate(@PathVariable Integer searchKeywordId, Model model) {
        SearchKeyword searchKeyword = searchKeywordService.selectById(searchKeywordId);
        model.addAttribute("item",searchKeyword);
        LogObjectHolder.me().set(searchKeyword);
        return PREFIX + "searchKeyword_edit.html";
    }

    /**
     * 获取搜索关键字管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return searchKeywordService.list(condition);
    }

    /**
     * 新增搜索关键字管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(SearchKeyword searchKeyword) {
        searchKeywordService.insert(searchKeyword);
        return SUCCESS_TIP;
    }

    /**
     * 删除搜索关键字管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer searchKeywordId) {
        searchKeywordService.deleteById(searchKeywordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改搜索关键字管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(SearchKeyword searchKeyword) {
        searchKeywordService.updateById(searchKeyword);
        return SUCCESS_TIP;
    }

    /**
     * 搜索关键字管理详情
     */
    @RequestMapping(value = "/detail/{searchKeywordId}")
    @ResponseBody
    public Object detail(@PathVariable("searchKeywordId") Integer searchKeywordId) {
        return searchKeywordService.selectById(searchKeywordId);
    }
}
