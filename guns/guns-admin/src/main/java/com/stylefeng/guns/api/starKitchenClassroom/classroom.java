package com.stylefeng.guns.api.starKitchenClassroom;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.banner.service.IBannerService;
import com.stylefeng.guns.modular.classroom.service.IClassroomService;
import com.stylefeng.guns.modular.system.model.Banner;
import com.stylefeng.guns.modular.system.model.Classroom;
import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.tag.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by Heyifan Cotter on 2018/10/23. 星厨课堂前台接口
 */
@RequestMapping("professionalVideo")
@Controller
public class classroom {

    @Autowired
    private ITagService tagService;

    @Autowired
    private IClassroomService classroomService;

    @Autowired
    private IBannerService bannerService;

    /**
     * 星厨课堂首页
     *
     * @return
     */
    @RequestMapping("coverPage")
    @ResponseBody
    ResultMsg coverPage() {
        /**
         * 封装整体的返回对象
         */
        Map<String, Object> heyifanMap = new HashMap<>();
        String tagName = "";
        try {
            /**
             *广告图片的数组
             */
            Map<String, Object> bannerMap = new HashMap<>();
            List<Map<String,Object>> bannerTempList = new ArrayList<>();
            EntityWrapper<Banner> bannerEntityWrapper = new EntityWrapper<>();
            bannerEntityWrapper.where("classroom_id is  not  null ");
            List<Banner> banners = bannerService.selectList(bannerEntityWrapper);
            if (banners.size() > 0)  {
                for (Banner banner : banners) {
                    bannerMap.put("videoId", banner.getItem_id());
                    bannerMap.put("picture", banner.getPicture());
                    bannerTempList.add(bannerMap);
                }
            } else {
            }
            /**
             * 标签数组封装
             */
            Map<String, Object> tagMap = new HashMap<>();
            List<Map<String,Object>> tagTempList = new ArrayList<>();
            EntityWrapper<Tag> tagEntityWrapper = new EntityWrapper<>();
            tagEntityWrapper.where("column_id={0} or column_id={1}", 25, 27);
            List<Tag> tags = tagService.selectList(tagEntityWrapper);
            if (tags.size() > 0) {
                for (Tag tag : tags) {
                    tagMap.put("name", tag.getName());
                    tagMap.put("picture", tag.getPicture());
                    tagTempList.add(tagMap);
                }
            } else {
            }

            /**
             * 推荐视频数组
             */
            Map<String, Object> recommendMap = new HashMap<>();
            List<Map<String,Object>> recommendTempList = new ArrayList<>();
            EntityWrapper<Classroom> classroomEntityWrapper = new EntityWrapper<>();
            classroomEntityWrapper.where("column_id={0}" ,27).and("coverphoto is not null ").orderDesc(Collections.singleton("create_time"));
            List<Classroom> classroomList = classroomService.selectList(classroomEntityWrapper);
            if (classroomList.size() > 0) {

                for (Classroom classroom : classroomList) {
                    recommendMap.put("coverPhoto", classroom.getCoverphoto());
                    recommendMap.put("shorTitle", classroom.getShorTitle());
                    recommendMap.put("videoId", classroom.getId());
                    recommendTempList.add(recommendMap);
                }
            } else {
            }

            /**
             * 视频集合
             */
            Map<String, Object> videoMap = new HashMap<>();
            List<Map<String,Object>> videoTempList = new ArrayList<>();
            EntityWrapper<Classroom> classroomEntityWrapperList = new EntityWrapper<>();
            classroomEntityWrapperList.orderDesc(Collections.singleton("create_time"));
            List<Classroom> videoMapList = classroomService.selectList(classroomEntityWrapperList);
            if (videoMapList.size() > 0) {
                for (Classroom classroom : videoMapList) {
                    videoMap.put("thumb", classroom.getThumb());
                    videoMap.put("title", classroom.getTitle());
                    videoMap.put("id", classroom.getId());
                    String[] heyifanArr = classroom.getTagId().split(",");
                    for (String heyifan : heyifanArr) {
                        if (heyifan.equals("5")) {
                        } else if (heyifan.equals("6")) {
                        } else {
                            tagName = (tagService.selectById(Integer.parseInt(heyifan))).getName();
                        }
                    }
                    videoMap.put("tagName", tagName);
                    videoMap.put("numberLearing", classroom.getNumberLearning());
                    videoTempList.add(videoMap);
                }

            } else {
            }
            /**
             * 电子菜谱数组集合
             */
            Map<String, Object> recipesMap = new HashMap<>();
            List<Map<String,Object>> recipsTempList = new ArrayList<>();
            EntityWrapper<Classroom> recipesEntity = new EntityWrapper<>();
            recipesEntity.where("column_id={0}", 25);
            List<Classroom> recipesMapList = classroomService.selectList(recipesEntity);
            if (recipesMapList.size() > 0) {
                for (Classroom classroom : recipesMapList) {
                    recipesMap.put("title", classroom.getTitle());
                    recipesMap.put("thmb", classroom.getThumb());
                    recipesMap.put("descritpion", classroom.getDescription());
                    recipesMap.put("recipesId", classroom.getId());
                    String[] heyifanArr = classroom.getTagId().split(",");
                    for (String heyifan : heyifanArr) {
                        if (heyifan.equals("5")) {
                        } else if (heyifan.equals("6")) {
                        } else {
                            tagName = (tagService.selectById(Integer.parseInt(heyifan))).getName();
                        }
                    }
                    recipesMap.put("tagName",tagName);
                    recipsTempList.add(recipesMap);
                }
            }
                heyifanMap.put("bannerMap",bannerTempList);
                heyifanMap.put("tagMap",tagTempList);
                heyifanMap.put("recommendMap",recommendTempList);
                heyifanMap.put("videoMap",videoTempList);
                heyifanMap.put("recipesMap",recipsTempList);

        } catch (NumberFormatException e) {
            e.printStackTrace();
//            return ResultMsg.fail("接口访问失败!", HttpStatus.BAD_REQUEST.toString(),"");
        }


        return ResultMsg.success("接口访问成功!", HttpStatus.OK.toString(),heyifanMap);
    }

}
