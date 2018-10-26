package com.stylefeng.guns.api.starKitchenClassroom;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.banner.service.IBannerService;
import com.stylefeng.guns.modular.classroom.service.IClassroomService;
import com.stylefeng.guns.modular.picture.service.IPictureService;
import com.stylefeng.guns.modular.system.model.Banner;
import com.stylefeng.guns.modular.system.model.Classroom;
import com.stylefeng.guns.modular.system.model.Picture;
import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.tag.service.ITagService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private IPictureService pictureService;

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
            List<Map<String, Object>> bannerTempList = new ArrayList<>();
            EntityWrapper<Banner> bannerEntityWrapper = new EntityWrapper<>();
            bannerEntityWrapper.where("item_id is  not  null");
            List<Banner> banners = bannerService.selectList(bannerEntityWrapper);
            if (banners.size() > 0) {
                for (Banner banner : banners) {
                    Map<String, Object> bannerMap = new HashMap<>();
                    bannerMap.put("videoId", banner.getItem_id());
                    bannerMap.put("picture", banner.getPicture());
                    bannerTempList.add(bannerMap);
                }
            } else {
            }
            /**
             * 标签数组封装
             */
            List<Map<String, Object>> tagTempList = new ArrayList<>();
            EntityWrapper<Tag> tagEntityWrapper = new EntityWrapper<>();
            tagEntityWrapper.where("column_id={0} or column_id={1}", 25, 27).orderBy("sort");
            List<Tag> tags = tagService.selectList(tagEntityWrapper);
            if (tags.size() > 0) {
                for (Tag tag : tags) {
                    Map<String, Object> tagMap = new HashMap<>();
                    tagMap.put("name", tag.getName());
                    tagMap.put("picture", tag.getPicture());
                    tagTempList.add(tagMap);
                }
            } else {
            }

            /**
             * 推荐视频数组
             */
            List<Map<String, Object>> recommendTempList = new ArrayList<>();
            EntityWrapper<Classroom> classroomEntityWrapper = new EntityWrapper<>();
            classroomEntityWrapper.where("column_id={0}", 27).and("coverphoto is not null ").orderDesc(Collections.singleton("create_time"));
            List<Classroom> classroomList = classroomService.selectList(classroomEntityWrapper);
            if (classroomList.size() > 0) {
                for (Classroom classroom : classroomList) {
                    Map<String, Object> recommendMap = new HashMap<>();
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
            List<Map<String, Object>> videoTempList = new ArrayList<>();
            EntityWrapper<Classroom> classroomEntityWrapperList = new EntityWrapper<>();
            classroomEntityWrapperList.orderDesc(Collections.singleton("create_time"));
            List<Classroom> videoMapList = classroomService.selectList(classroomEntityWrapperList);
            if (videoMapList.size() > 0) {
                for (Classroom classroom : videoMapList) {
                    Map<String, Object> videoMap = new HashMap<>();
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

            List<Map<String, Object>> recipsTempList = new ArrayList<>();
            EntityWrapper<Classroom> recipesEntity = new EntityWrapper<>();
            recipesEntity.where("column_id={0}", 25);
            List<Classroom> recipesMapList = classroomService.selectList(recipesEntity);
            if (recipesMapList.size() > 0) {
                for (Classroom classroom : recipesMapList) {
                    Map<String, Object> recipesMap = new HashMap<>();
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
                    recipesMap.put("tagName", tagName);
                    recipsTempList.add(recipesMap);
                }
            }
            heyifanMap.put("bannerMap", bannerTempList);
            heyifanMap.put("tagMap", tagTempList);
            heyifanMap.put("recommendMap", recommendTempList);
            heyifanMap.put("videoMap", videoTempList);
            heyifanMap.put("recipesMap", recipsTempList);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        return ResultMsg.success("接口访问成功!", HttpStatus.OK.toString(), heyifanMap);
    }

    /**
     * 获取全部星厨课堂视屏列表
     */
    @RequestMapping("getAllStartVideo")
    @ResponseBody
    ResultMsg getAllStartVideo(@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                               @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo) {
        Map<String, Object> heyifanMap = null;
        try {
            heyifanMap = new HashMap<>();
            List<Map<String, Object>> mapForVideo = new ArrayList<>();
            String tagName = "";
            List<Classroom> classroomList = classroomService.selectPage(
                    new Page<>(pageNo, pageSize),
                    new EntityWrapper<Classroom>().orderDesc(Collections.singleton("create_time"))
            ).getRecords();

            for (Classroom classroom : classroomList) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("id", classroom.getId());
                tempMap.put("thumb", classroom.getThumb());
                tempMap.put("title", classroom.getTitle());
                tempMap.put("time", classroom.getCreateTime());
                tempMap.put("numberLearing", "");
                String[] heyifanArr = classroom.getTagId().split(",");
                for (String heyifan : heyifanArr) {
                    if (heyifan.equals("5")) {
                    } else if (heyifan.equals("6")) {
                    } else {
                        tagName = (tagService.selectById(Integer.parseInt(heyifan))).getName();
                    }
                }
                tempMap.put("tagName", tagName);
                mapForVideo.add(tempMap);
            }
            heyifanMap.put("allVideoMap", mapForVideo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ResultMsg.fail("接口调用失败!", HttpStatus.BAD_REQUEST.toString(), "");
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), heyifanMap);
    }

    /**
     * 根据传入的id来获得视频课程详情
     *
     * @param id 传入的视频id
     * @return 视频详情页面需要的map数据
     */
    @RequestMapping("getVideoInfoById")
    @ResponseBody
    ResultMsg getVideoInfoById(Integer id) {
        Map<String, Object> heyifanMap = null;
        List<Map<String, Object>> videoInfoList = null;
        try {
            heyifanMap = new HashMap<>();
            videoInfoList = new ArrayList<>();
            Integer tagId = -1;
            Classroom classroom = classroomService.selectById(id);
            if (classroom != null) {
                Map<String, Object> tempVideo = new HashMap<>();
                tempVideo.put("title", classroom.getTitle());
                String[] heyifanArr = classroom.getTagId().split(",");
                /**
                 * 封装视频url
                 */
                if (classroom.getVideo() != null) {
                    EntityWrapper<Picture> entityWrapper = new EntityWrapper<>();
                    entityWrapper.eq("base_id", classroom.getVideo());
                    List<Picture> pictures = pictureService.selectList(entityWrapper);
                    tempVideo.put("videoHttp", pictures.get(0));
                }
                /**
                 * 分类id
                 */
                for (String heyifan : heyifanArr) {
                    if (heyifan.equals("5")) {
                    } else if (heyifan.equals("6")) {
                    } else {
                        tagId = Integer.parseInt(heyifan);
                    }
                }
                tempVideo.put("tagId", tagId);
                tempVideo.put("description", classroom.getDescription());
                //评论总数
                tempVideo.put("commitCount", "");
                //点赞总数
                tempVideo.put("likeCount", "");
                videoInfoList.add(tempVideo);
            } else {

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResultMsg.unSuccess("接口调用失败!");
        }
        heyifanMap.put("videoMapInfo", videoInfoList);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), heyifanMap);
    }


    /**
     * 点击猜猜喜欢课程跳转的页面
     */
    @RequestMapping("clickGuess")
    @ResponseBody
    ResultMsg clickGuess(@RequestParam(value = "id", required = true) Integer id, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                         @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo) {
        Map<String, Object> heyifanMap = new HashMap<>();
        List<Map<String, Object>> tagList = new ArrayList<>();
        List<Map<String, Object>> contentInfo = new ArrayList<>();
        List<Map<String, Object>> userInfo = new ArrayList<>();

        String tagName = tagService.selectById(id).getName();
        try {
/**
 * 分页排序获得传入id分类列表
 */
            List<Classroom> classroomList = classroomService.selectPage(
                    new Page<>(pageNo, pageSize),
                    new EntityWrapper<Classroom>().like("tag_id", id.toString())

            ).getRecords();

            if (classroomList.size() > 0) {
                for (Classroom classroom : classroomList) {
                    Map<String, Object> tempList = new HashMap<>();
                    tempList.put("tagName", tagName);
                    tempList.put("title", classroom.getTitle());
                    tempList.put("description", classroom.getDescription());
                    tempList.put("thumb", classroom.getThumb());
                    tempList.put("browseCount", "");
                    tempList.put("likeCount", "");
                    tagList.add(tempList);
                }
            }

            if (classroomList.size() > 0) {
                Map<String, Object> contentTemp = new HashMap<>();
                contentTemp.put("content", classroomList.get(0).getContent());
                contentInfo.add(contentTemp);
            }
            if (classroomList.size() > 0) {
                Map<String, Object> userTemp = new HashMap<>();
                userTemp.put("userDescription", classroomList.get(0).getUserDescription());
                userInfo.add(userTemp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("调用接口失败!", HttpStatus.BAD_REQUEST.toString(), "");
        }

        return ResultMsg.success("调用接口成功!", HttpStatus.OK.toString(), "");
    }


}
