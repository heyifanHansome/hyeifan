package com.stylefeng.guns.api.dynamic;

import com.stylefeng.guns.core.util.ResultMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/10/29.
 * 星厨动态前台接口
 */
@Api(value = "星厨动态前台接口", tags = "星厨动态前台接口")
public class dynamicApi {
//    @ApiOperation(value = "", tags = "")
//    @RequestMapping(value = "clickDynamic", method = RequestMethod.POST);
    ResultMsg clickDynamic() {

        Map<String, Object> heyifanMap = new HashMap<>();
        List<Map<String, Object>> tempList = new ArrayList<>();


        Map<String,Object> tempMap = new HashMap<>();


        return ResultMsg.success("调用接口成功!", HttpStatus.OK.toString(), heyifanMap);
    }
}
