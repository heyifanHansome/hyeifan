package com.stylefeng.guns.api.learSkill;

import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.ruleModel.service.IRuleService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import com.stylefeng.guns.modular.userTarget.service.IUserTargetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Heyifan Cotter on 2018/10/30.
 * 拜师学艺前台接口
 */
@Api(tags = "拜师学艺前台接口")
@RequestMapping("learSkill")
@RestController
public class learSkillApi {

    @Autowired
    private IRuleService ruleService;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IUserTargetService userTargetService;
    @Autowired
    private IUserInfoService userInfoService;


    @RequestMapping(value = "getRuleHtml", method = RequestMethod.POST)
    ResultMsg getRuleHtml() {

        List<Rule> ruleList = ruleService.selectList(null);
        ruleList.get(0).getContent();
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), ruleList.get(0).getContent());

    }

    @ApiOperation("")
    @RequestMapping("openDiscipleContent")
    ResultMsg openDiscipleContent() {
        List<Rule> ruleList = ruleService.selectList(null);
        ruleList.get(0).getRule();
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), ruleList.get(0).getRule());
    }


    @RequestMapping("checkOpenCondition")
    ResultMsg checkOpenCondition(Integer userId) {
        boolean flag = false;
        UserInfo userInfo = userInfoService.selectById(userId);
        UserTarget userTarget = userTargetService.selectById(userId);
        if (Integer.parseInt(userTarget.getLv()) > 4 && userInfo.getJoinClub() == 1) {
            flag = true;
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), flag);
    }


}
