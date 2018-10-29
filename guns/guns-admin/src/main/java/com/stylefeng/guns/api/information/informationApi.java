package com.stylefeng.guns.api.information;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.infomation.service.IInformationService;
import com.stylefeng.guns.modular.system.model.Information;
import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.tag.service.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 资讯前台接口
 * Created by Heyifan Cotter on 2018/10/29.
 */
@Api(tags = "资讯前台接口")
@RequestMapping("informationApi")
@RestController
public class informationApi {
    @Autowired
    private IInformationService informationService;
    @Autowired
    private ITagService tagService;

    @RequestMapping(value = "clickInformation", method = RequestMethod.POST)
    @ApiOperation(value = "点击跳转资讯类表页面", notes = "点击跳转资讯类表页面")
    @ApiResponses(@ApiResponse(code = 200, message = "{\n" +
            "    \"data\": {\n" +
            "        \"informationMap\": \"资讯封装返回集合\"\"\n" +
            "            {\n" +
            "                \"thumb\": \"缩略图\",\n" +
            "                \"browseCount\": \"浏览量\",\n" +
            "                \"description\": \"描述\",\n" +
            "                \"likeCount\": \"点赞量\",\n" +
            "                \"title\": \"标题\",\n" +
            "                \"tagName\": \"分类名称!\"\n" +
            "            },\n" +
            "    },\n" +
            "    \"detail\": \"200\",\n" +
            "    \"message\": \"接口调用成功!\",\n" +
            "    \"success\": \"ok\"\n" +
            "}")
    )
    ResultMsg clickInformation() {
        Map<String, Object> heyifanMap = new HashMap<>();
        List<Map<String, Object>> informationMap = new ArrayList<>();
        try {
            String tagName = "";
            EntityWrapper<Information> entityWrapper = new EntityWrapper<>();
            entityWrapper.orderDesc(Collections.singleton("create_time"));
            List<Information> informationList = informationService.selectList(entityWrapper);

            for (Information information : informationList) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("id", information.getId());
                tempMap.put("title", information.getTitle());
                tempMap.put("thumb", information.getThumb());
                tempMap.put("description", information.getDescription());
                tempMap.put("browseCount", "");
                tempMap.put("likeCount", "");
                String[] heyifanArr = information.getTagId().split(",");
                for (String heyifan : heyifanArr) {
                    if (heyifan.equals("5")) {
                    } else if (heyifan.equals("6")) {
                    } else {
                        if (!heyifan.equals("")) {
                            Tag tag = tagService.selectById(Integer.parseInt(heyifan));
                            if (tag != null) {
                                tagName = tag.getName();
                            }
                        }
                    }
                }
                tempMap.put("tagName", tagName);
                informationMap.add(tempMap);
            }
            heyifanMap.put("informationMap", informationMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("", HttpStatus.BAD_REQUEST.toString(), "");
        }
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), heyifanMap);
    }


    @ApiOperation(value = "点击进入星厨资讯详情页面", notes = "点击进入星厨资讯详情页面接口")
    @ApiResponses(@ApiResponse(code = 200, message = "{\n" +
            "    \"data\": {\n" +
            "        \"columnType\": \"栏目名称\",\n" +
            "        \"createTime\": \"创建时间\",\n" +
            "        \"browseCount\": \"浏览量\",\n" +
            "        \"titlePicture\": \"封面图片\",\n" +
            "        \"likeCount\": \"点赞\",\n" +
            "        \"title\": \"标题\",\n" +
            "        \"content\"\"富文本类容\"\n" +
            "    },\n" +
            "    \"detail\": \"200\",\n" +
            "    \"message\": \"接口调用成功!\",\n" +
            "    \"success\": \"ok\"\n" +
            "}"))
    @RequestMapping(value = "getInformationById", method = RequestMethod.POST)
    ResultMsg getInformationById(Integer id) {
        Information information = informationService.selectById(id);
        Map<String, Object> informationMap = new HashMap<>();
        informationMap.put("titlePicture", information.getThumb());
        informationMap.put("title", information.getTitle());
        informationMap.put("createTime", information.getCreateTime());
        informationMap.put("columnType", "星厨资讯");
        informationMap.put("content",information.getContent());
        informationMap.put("browseCount", "");
        informationMap.put("likeCount", "");
        return ResultMsg.success("接口调用成功!", HttpStatus.OK.toString(), informationMap);
    }


}
