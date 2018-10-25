package com.stylefeng.guns.api.starKitchenClassroom;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.classroom.service.IClassroomService;
import com.stylefeng.guns.modular.system.model.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Heyifan Cotter on 2018/10/23. 星厨课堂前台接口
 */
@RequestMapping("professionalVideo")
@Controller
public class classroom {

    @Autowired
    private IClassroomService classroomService;
    /**
     * @return
     */
    @RequestMapping("coverPage")
    ResultMsg coverPage() {
        /**
         * 封装整体的返回对象
         */
        Map<String, Object> heyifanMap = new HashMap<>();
        /**
         *广告图片的数组
         */
        Map<String, Object> bannerMap = new HashMap<>();
        /**
         * 标签数组封装
         */
        Map<String, Object> tagMap = new HashMap<>();

        /**
         * 推荐视频数组
         */
        Map<String, Object> recommendMap = new HashMap<>();
        EntityWrapper<Classroom>   classroomEntityWrapper = new EntityWrapper<>();
        classroomEntityWrapper.where("coverphoto is not  null order by create_time desc");
        List<Classroom> classroomList = classroomService.selectList(classroomEntityWrapper);
        if(classroomList.size() >4){

        }else {
            recommendMap.put("recommendMap","没有足够的数据展示");
        }


        /**
         * 视频集合
         */
        Map<String, Object> videoMap = new HashMap<>();


        /**
         * 电子菜谱数组集合
         */
        Map<String, Object>  recipesMap = new HashMap<>();



        return ResultMsg.success("", "", "");
    }


}
