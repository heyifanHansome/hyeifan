package com.stylefeng.guns.api.classroom;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.core.util.Time;
import com.stylefeng.guns.modular.classroom.service.IClassroomService;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.tag.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * Created by Heyifan Cotter on 2018/10/13.
 */
@RequestMapping("classroomApi")
@RestController
public class classroomApiInteface {
    @Autowired
    private IClassroomService classroomService;

    @Autowired
    private IColumnTypeService columnTypeService;

    @Autowired
    private ITagService tagService;

    @Autowired
    private IUserApiService userApiService;

    /**
     * 根据传入的栏目id获得当前的所有数据
     *
     * @param value 前台传入的id
     * @return 根据当前的id 获得当前数据
     */
    @RequestMapping("getAllClassRoom/{value}")
    ResultMsg getAllClassRoom(@PathVariable("value") Integer value) {
        try {
            PageHelper pageHelper = new PageHelper();
            pageHelper.startPage(1, 2);

            EntityWrapper<Classroom> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("column_id", value);
            List<Classroom> classrooms = classroomService.selectList(entityWrapper);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.success("获取失败!", "code : 500", HttpStatus.BAD_REQUEST);
        }
        return ResultMsg.success("获取成功!", "code : 200", HttpStatus.OK);
    }

    @RequestMapping("clickClassRoom")
    ResultMsg getStartClassroom() {
        try {
            //返回结果集
            Map<String, Map<String, Object>> myMap = new HashMap<>();
            //对应的集体
            Map<String, Object> map = new HashMap<>();
            EntityWrapper<ColumnType> columnTypeEntityWrapper = new EntityWrapper<>();
            columnTypeEntityWrapper.eq("menu_id", "1049863045267951779");
            List<ColumnType> columnTypes = columnTypeService.selectList(columnTypeEntityWrapper);
            //封装文字集合
            List<String> titleName = new ArrayList<>();
            //封装视频集合
            Map<String, Object> videoMap = new HashMap<>();

            //封装电子菜谱
            Map<String, Object> electronicRecipesMap = new HashMap<>();

            //封装电子菜谱
            Map<String, Object> masterFoodMap = new HashMap<>();

            if (columnTypes.size() > 0) {
                for (int i = 0; i < columnTypes.size(); i++) {
                    titleName.add(columnTypes.get(i).getName());
                }
            }
            map.put("firstPage", titleName);

            EntityWrapper<Classroom> classroomEntityWrapper = new EntityWrapper<>();
            classroomEntityWrapper.where("column_id", 27).orderDesc(Collections.singleton("create_time"));
            List<Classroom> classrooms = classroomService.selectList(classroomEntityWrapper);

            if (classrooms.size() > 4) {
                for (int i = 0; i < 4; i++) {
                    videoMap.put("titleName", classrooms.get(i).getTitle());
                    videoMap.put("description", classrooms.get(i).getDescription());
                    videoMap.put("thumb", classrooms.get(i).getThumb());
                }
            } else {
                videoMap.put("enough", "视频请传入4个以上!");
            }
            map.put("RecommendVideo", videoMap);

            EntityWrapper<Classroom> electronicRecipesEntityWarpper = new EntityWrapper<>();
            electronicRecipesEntityWarpper.where("column_id", 25).orderDesc(Collections.singleton("create_time"));
            List<Classroom> electronicRecipesEntity = classroomService.selectList(classroomEntityWrapper);

            Time date = new Time();
            if (electronicRecipesEntity.size() > 4) {
                for (int i = 0; i < 4; i++) {
                    electronicRecipesMap.put("titleName", electronicRecipesEntity.get(i).getTitle());
                    electronicRecipesMap.put("description", electronicRecipesEntity.get(i).getDescription());
                    electronicRecipesMap.put("thumb", electronicRecipesEntity.get(i).getThumb());
                    electronicRecipesMap.put("currentTime", date.CalculateTime(electronicRecipesEntity.get(i).getCreateTime().toString()));
                }
            } else {
                electronicRecipesMap.put("enough", "视频请传入4个以上!");
            }

            map.put("electronicRecipesMap", electronicRecipesMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("查询失败!", "code : 500", HttpStatus.BAD_REQUEST);
        }


        return ResultMsg.success("查询成功!", "code : 200", HttpStatus.OK);

    }


    @RequestMapping("classroom/pageInfo")
    @ResponseBody
    ResultMsg getPageinfo() {
        try {
            Map<String, Map<String, Object>> heyifanMap = new HashMap<>();

            //封装视频标签图 集合
            EntityWrapper<Tag> tagEntityWrapper = new EntityWrapper<>();
            tagEntityWrapper.eq("column_id", "27");
            List<Tag> tags = tagService.selectList(tagEntityWrapper);
            Map<String, Object> tagList = new HashMap<>();
            if (tags.size() > 0) {
                for (int i = 0; i < tags.size(); i++) {
                    tagList.put("name", tags.get(i).getName());
                    tagList.put("picture", tags.get(i).getPicture());
                }
            }
            //添加标签集合
            heyifanMap.put("tagList", tagList);
            //定义推荐视频Map集合
            Map<String, Object> classRoomVideo = new HashMap<>();
            //条件查询获取推荐视频
            EntityWrapper<Classroom> classroomEntityWrapper = new EntityWrapper<>();
            classroomEntityWrapper.eq("colum_id", 27).orderDesc(Collections.singleton("create_time"));
            List<Classroom> classrooms = classroomService.selectList(classroomEntityWrapper);

            //推荐视频集合封装类
            if (classrooms.size() > 0) {
                for (int i = 0; i < classrooms.size(); i++) {
                    classRoomVideo.put("thumb", classrooms.get(i).getThumb());
                    classRoomVideo.put("title", classrooms.get(i).getTitle());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("调用失败!", "code : 500", HttpStatus.BAD_REQUEST);
        }
        return ResultMsg.success("调用成功!", "code : 200", HttpStatus.OK);
    }


    /**
     * 分页查询全部课程列表
     *
     * @param pageSize 页大小
     * @param pageNo   页码
     * @return 相应的数据
     */
    @RequestMapping("getAllClassroom")
    @ResponseBody
    ResultMsg getAllClassroom(@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                              @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo) {

        try {
            List<Classroom> classrooms = classroomService.selectPage(
                    new Page<>(pageNo, pageSize)
            ).getRecords();
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < classrooms.size(); i++) {
                map.put("thmub",classrooms.get(i).getTitle());
                map.put("title",classrooms.get(i).getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("", "", HttpStatus.BAD_REQUEST);
        }

        return ResultMsg.success("", "", HttpStatus.OK);
    }


}
