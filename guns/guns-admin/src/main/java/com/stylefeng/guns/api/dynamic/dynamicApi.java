package com.stylefeng.guns.api.dynamic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.city.service.ICityService;
import com.stylefeng.guns.modular.fans.service.IFansService;
import com.stylefeng.guns.modular.follow.service.IFollowService;
import com.stylefeng.guns.modular.picture.service.IPictureService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import com.stylefeng.guns.modular.userTarget.service.IUserTargetService;
import com.stylefeng.guns.modular.works.service.IWorksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * Created by Heyifan Cotter on 2018/10/29.
 * 星厨动态前台接口
 */
@Api(value = "星厨动态前台接口", tags = "星厨动态前台接口")
@RequestMapping("dynamicApi")
public class dynamicApi {
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IFollowService followService;
    @Autowired
    private IFansService fansService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserTargetService targetService;
    @Autowired
    private ICityService cityService;
    @Autowired
    private IWorksService worksService;
    @Autowired
    private IPictureService pictureService;


    @ApiOperation(value = "无关注的动态接口", notes = "无关注的动态接口")
    @ApiResponses(@ApiResponse(code = 200, message = ""))
    ResultMsg clickDynamic() {

        Map<String, Object> heyifanMap = new HashMap<>();
        List<Map<String, Object>> tempList = new ArrayList<>();

        try {
            /**
             * 封装查询条件查询当前用户发布作品
             */
            EntityWrapper<UserApi> entityWrapper = new EntityWrapper<>();
            entityWrapper.orderDesc(Collections.singleton("create_time"));
            List<UserApi> userApiList = userApiService.selectList(entityWrapper);


            for (UserApi userApi : userApiList) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("username", userApi.getName());
                UserTarget userTarget = targetService.selectById(userApi.getId());
                if (userTarget != null) {
                    tempMap.put("lv", userTarget.getLv());
                }
                EntityWrapper<Works> worksEntityWrapper = new EntityWrapper<>();
                worksEntityWrapper.eq("user_id", userApi.getId());
                List<Works> worksList = worksService.selectList(worksEntityWrapper);
                if (worksList.size() > 0) {
                    tempMap.put("worksCount", worksList.size());
                }
                for (int i = 0; i < 3; i++) {
                    List<Map<String, Object>> thumbList = new ArrayList<>();
                    Map<String, Object> thumbMap = new HashMap<>();
                    thumbMap.put("id", worksList.get(i).getId());
                    EntityWrapper<Picture> pictureEntityWrapper = new EntityWrapper<>();
                    pictureEntityWrapper.eq("base_id", worksList.get(i).getImages());
                    List<Picture> pictureList = pictureService.selectList(pictureEntityWrapper);
                    if (pictureList.size() > 0) {
                        thumbMap.put("thumb", pictureList.get(0).getOssObjectName());
                    }
                    thumbList.add(thumbMap);
                    tempMap.put("thumbMap", thumbList);

                }
                EntityWrapper<City> cityEntityWrapper = new EntityWrapper<>();
                List<City> cityList = cityService.selectList(cityEntityWrapper);
                tempMap.put("cityName", cityList.get(0).getName());
                tempMap.put("fans", "");
                tempMap.put("follow", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultMsg.success("调用接口成功!", HttpStatus.OK.toString(), heyifanMap);
    }


    @RequestMapping(value = "postNew", method = RequestMethod.POST)
    ResultMsg postNew(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
        List<Map<String, Object>> heyifanMap = new ArrayList<>();


        EntityWrapper<Works> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderDesc(Collections.singleton("create_time"));
        List<Works> worksList = worksService.selectList(entityWrapper);

        Date firstCreateTime = worksList.get(0).getCreateTime();


        EntityWrapper<Works> worksEntityWrapper = new EntityWrapper<>();
        worksEntityWrapper.where(("DATEDIFF( {0}, sys_works.create_time) = {1}") == "0", "sys_works.create_time");
        List<Works> works = worksService.selectList(worksEntityWrapper);


        return ResultMsg.success("调用接口成功!", HttpStatus.OK.toString(), heyifanMap);


    }
}
