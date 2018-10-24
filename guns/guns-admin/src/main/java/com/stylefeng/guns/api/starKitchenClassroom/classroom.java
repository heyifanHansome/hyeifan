package com.stylefeng.guns.api.starKitchenClassroom;

import com.stylefeng.guns.core.util.ResultMsg;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/10/23. 星厨课堂前台接口
 */
@RequestMapping("professionalVideo")
@Controller
public class classroom {

    @RequestMapping("coverPage")
    ResultMsg coverPage() {
        /**
         * 封装整体的返回对象
         */
        Map<String, Object> heyifanMap = new HashMap<>();
        /**
         *
         */
        Map<String, Object> bannerMap = new HashMap<>();
        Map<String, Object> recommendMap = new HashMap<>();

        return  ResultMsg.success("","","");
    }


}
