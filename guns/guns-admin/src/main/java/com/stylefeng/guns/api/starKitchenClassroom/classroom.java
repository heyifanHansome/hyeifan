package com.stylefeng.guns.api.starKitchenClassroom;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.banner.service.IBannerService;
import com.stylefeng.guns.modular.classroom.service.IClassroomService;
import com.stylefeng.guns.modular.picture.service.IPictureService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.system.service.IStudyLogService;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.userFabulous.service.IUserFabulousService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 星厨课堂前台接口
 * Created by Heyifan Cotter on 2018/10/23.
 */
@Api(tags = "星厨课堂接口")
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
    @Autowired
    private IUserFabulousService userFabulousService;
    @Autowired
    private IStudyLogService studyLogService;

    /**
     * 星厨课堂首页
     *
     * @return
     */
    @ApiOperation(value = "获取星厨课堂首页的数据", notes = "点击星厨课堂需要调用的后台接口")
    @ApiResponses(@ApiResponse(code = 200, message = "\"data\": {\n" +
            "\t\"videoMap\" + '视频Map': [\n" +
            "\t\t{\n" +
            "\t\t\t\"column_id\": 不用管这个字段, \n" +
            "\t\t\t\"numberLearing\": \"学习人数\",\n" +
            "\t\t\t\"thumb\": \"缩略图Url\",\n" +
            "\t\t\t\"id\": \"当前视频id\"\n" +
            "\t\t\t\"title\": \"视频标题\"\n" +
            "\t\t\t\"tagName\": \"分类名称\"\n" +
            "\t\t}\n" +
            "\t\n" +
            "\t],\n" +
            "\t\"recommendMap\": \"推荐视频Map\" [\n" +
            "\t\t{\n" +
            "\t\t\t\"shorTitle\": \"短标题\",\n" +
            "\t\t\t\"coverPhoto\": \"封面图\",\n" +
            "\t\t\t\"videoId\": \"当前视频id\"\n" +
            "\t\t}\n" +
            "\n" +
            "\t],\n" +
            "\t\"bannerMap\":\"广告Map\" [\n" +
            "\t\t{\n" +
            "\t\t\t\"videoId\": \"跳转视频的id\",\n" +
            "\t\t\t\"picture\": \"图片url\"\n" +
            "\t\t},\n" +
            "\t\n" +
            "\t],\n" +
            "\t\"tagMap\": [\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"分类名称\",\n" +
            "\t\t\t\"sort\": \"排序\",\n" +
            "\t\t\t\"picture\": \"图片url\"\n" +
            "\t\t},\n" +
            "\t\n" +
            "\t],\n" +
            "\t\"recipesMap\": \"电子菜谱Map\" [\n" +
            "\t\t{\n" +
            "\t\t\t\"browseCount\": \"浏览量\",\n" +
            "\t\t\t\"descritpion\": \"描述\",\n" +
            "\t\t\t\"likeCount\": \"点赞数量\",\n" +
            "\t\t\t\"title\": \"标题\",\n" +
            "\t\t\t\"tagName\": \"分类名称\",\n" +
            "\t\t\t\"thmb\": \"封面图\",\n" +
            "\t\t\t\"recipesId\": \"电子菜谱id\"\n" +
            "\t\t}\n" +
            "\t]\n" +
            "},\n" +
            "\"detail\": \"200\",\n" +
            "\"message\": \"接口访问成功!\",\n" +
            "\"success\": \"ok\" "
    ))
    @RequestMapping(value = "coverPage", method = RequestMethod.POST)
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
            }
            /**
             * 标签数组封装
             */
            List<Map<String, Object>> tagTempList = new ArrayList<>();
            EntityWrapper<Tag> tagEntityWrapper = new EntityWrapper<>();
            tagEntityWrapper.where("column_id={0} or column_id={1}", 25, 27).orderDesc(Collections.singleton("sort"));
            List<Tag> tags = tagService.selectList(tagEntityWrapper);
            if (tags.size() > 0) {
                for (Tag tag : tags) {
                    Map<String, Object> tagMap = new HashMap<>();
                    tagMap.put("name", tag.getName());
                    tagMap.put("picture", tag.getPicture());
                    tagMap.put("sort", tag.getSort());
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
            }
            /**
             * 视频课程集合
             */
            List<Map<String, Object>> videoTempList = new ArrayList<>();
            EntityWrapper<Classroom> classroomEntityWrapperList = new EntityWrapper<>();
            classroomEntityWrapperList.where("column_id={0}", 27).orderDesc(Collections.singleton("create_time"));
            List<Classroom> videoMapList = classroomService.selectList(classroomEntityWrapperList);
            if (videoMapList.size() > 0) {
                for (Classroom classroom : videoMapList) {
                    Map<String, Object> videoMap = new HashMap<>();
                    videoMap.put("thumb", classroom.getThumb());
                    videoMap.put("title", classroom.getTitle());
                    videoMap.put("column_id", classroom.getColumnId());
                    videoMap.put("id", classroom.getId());
                    String[] heyifanArr = classroom.getTagId().split(",");
                    for (String heyifan : heyifanArr) {
                        if (heyifan.equals("5")) {
                        } else if (heyifan.equals("6")) {
                        } else {
                            Tag tag = tagService.selectById(Integer.parseInt(heyifan));
                            if (tag == null) {

                            } else {
                                tagName = tag.getName();
                            }

                        }
                    }
                    videoMap.put("tagName", tagName);
                    videoMap.put("numberLearing", classroom.getNumberLearning());
                    videoTempList.add(videoMap);
                }

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
                            Tag tag = tagService.selectById(Integer.parseInt(heyifan));
                            if (tag == null) {

                            } else {
                                tagName = tag.getName();
                            }
                        }
                    }
                    recipesMap.put("tagName", tagName);
                    recipesMap.put("browseCount", "");
                    EntityWrapper<UserFabulous> userFabulousEntityWrapper = new EntityWrapper<>();
                    userFabulousEntityWrapper.where("column_id={0} and works_id={1}", 25, classroom.getId());
                    List<UserFabulous> userFabulousList = userFabulousService.selectList(userFabulousEntityWrapper);
                    recipesMap.put("likeCount", userFabulousList.size());
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
     * 获取全部星厨课堂列表
     */
    @ApiOperation(value = "获取星厨课堂列表", notes = "点击获取星厨课堂类表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小", required = true, defaultValue = "10"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, defaultValue = "1")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "{\n" +
            "    \"data\": {\n" +
            "        \"allVideoMap\": [\n" +
            "            {\n" +
            "                \"numberLearing\": \"学习人数\",\n" +
            "                \"thumb\": \"缩略图url\",\n" +
            "                \"id\": \"视频id\",\n" +
            "                \"title\": \"视频标题\",\n" +
            "                \"tagName\": \"分类名称\"\n" +
            "            }\n" +
            "            \n" +
            "         \n" +
            "    },\n" +
            "    \"detail\": \"200\",\n" +
            "    \"message\": \"接口调用成功!\",\n" +
            "    \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getAllStartVideo",method = RequestMethod.POST)
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
                tempMap.put("numberLearing", "");
                String[] heyifanArr = classroom.getTagId().split(",");
                for (String heyifan : heyifanArr) {
                    if (heyifan.equals("5")) {
                    } else if (heyifan.equals("6")) {
                    } else {
                        Tag tag = tagService.selectById(Integer.parseInt(heyifan));
                        if (tag == null) {

                        } else {
                            tagName = tag.getName();
                        }
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
    @ApiOperation(value = "根据id进入详情页", notes = "根据id进入详情页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "传入id", required = true),
            @ApiImplicitParam(name = "phone", value = "当前用户电话号码", required = true)
    }
    )
    @ApiResponses(@ApiResponse(code = 200, message = "{\n" +
            "    \"data\": {\n" +
            "        \"userInfo\":\"作者详情页面需要的数据\" [\n" +
            "            {\n" +
            "                \"userDescription\": \"<p>ad</p>\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"videoMapInfo\": [\n" +
            "            {\n" +
            "                \"tagId\": -1,                \n" +
            "                \"description\": \"12\",\n" +
            "                \"commitCount\": \"\",\n" +
            "                \"likeCount\": \"\",\n" +
            "                \"title\": \"23\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"contentInfo\":\"图文需要的数据\" [\n" +
            "            {\n" +
            "                \"content\": \"<p>asd</p>\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"detail\": \"200\",\n" +
            "    \"message\": \"接口调用成功!\",\n" +
            "    \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getVideoInfoById", method = RequestMethod.POST)
    @ResponseBody
    ResultMsg getVideoInfoById(Integer id, String phone) {
        Integer newPhone = Integer.parseInt(phone.substring(0, phone.length() - 1));
        Map<String, Object> heyifanMap = null;
        List<Map<String, Object>> contentInfo = new ArrayList<>();
        List<Map<String, Object>> userInfo = new ArrayList<>();
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
                    if (pictures.size() > 0) {
                        tempVideo.put("videoHttp", pictures.get(0));
                    }
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
            if (classroom != null) {
                Map<String, Object> contentTemp = new HashMap<>();
                contentTemp.put("content", classroom.getContent());
                contentInfo.add(contentTemp);

            }
            if (classroom != null) {
                Map<String, Object> userTemp = new HashMap<>();
                userTemp.put("userDescription", classroom.getUserDescription());
                userInfo.add(userTemp);
            }
            //学习人数添加判断
            EntityWrapper<StudyLog> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("column_id={0} and works_id={1} and phone = {2}", classroom.getColumnId(), classroom.getId(), newPhone);
            List<StudyLog> list = studyLogService.selectList(entityWrapper);
            if (list.size() == 0) {
                StudyLog studyLog = new StudyLog();
                studyLog.setColumnId(classroom.getColumnId());
                studyLog.setWorksId(id);
                studyLog.setPhone(Integer.parseInt(phone.substring(0, phone.length() - 1)));
                studyLogService.insert(studyLog);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.unSuccess("接口调用失败!");
        }
        heyifanMap.put("videoMapInfo", videoInfoList);
        heyifanMap.put("userInfo", userInfo);
        heyifanMap.put("contentInfo", contentInfo);
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), heyifanMap);
    }


    /**
     * @param id       传入视频标签id
     * @param pageSize 分页大小
     * @param pageNo   分页页码
     * @return 封装好的Map集合
     */
    @ApiOperation(value = "猜猜可能喜欢的课程前台接口", notes = "猜猜可能喜欢的课程前台接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "出入视频相关的id", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", required = false, defaultValue = "10"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = false, defaultValue = "1"),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "{\n" +
            "    \"data\": {\n" +
            "        \"likeGuess\": [\n" +
            "\n" +
            "            {\n" +
            "                \"thumb\": \"缩略图\",\n" +
            "                \"browseCount\": \"浏览量\",\n" +
            "                \"description\": \"描述\",\n" +
            "                \"likeCount\": \"点赞量\",\n" +
            "                \"tagName\": \"分类名称\",\n" +
            "                \"title\": \"标题\"\n" +
            "            },\n" +
            "\n" +
            "        ]\n" +
            "    },\n" +
            "    \"detail\": \"200\",\n" +
            "    \"message\": \"调用接口成功!\",\n" +
            "    \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "clickGuess", method = RequestMethod.POST)
    @ResponseBody
    ResultMsg clickGuess(@RequestParam(value = "id", required = true) Integer id, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                         @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo) {
        Map<String, Object> heyifanMap = new HashMap<>();
        List<Map<String, Object>> tagList = new ArrayList<>();


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

        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("调用接口失败!", HttpStatus.BAD_REQUEST.toString(), "");
        }
        heyifanMap.put("likeGuess", tagList);
        return ResultMsg.success("调用接口成功!", HttpStatus.OK.toString(), heyifanMap);
    }


}
