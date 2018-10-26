package com.stylefeng.guns.api.usework;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.QRCodeUtil;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.core.util.Time;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.system.vo.commentVo;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.tagRelation.service.ITagRelationService;
import com.stylefeng.guns.modular.userCommentModel.service.IUserCommentService;
import com.stylefeng.guns.modular.userFabulous.service.IUserFabulousService;
import com.stylefeng.guns.modular.works.service.IWorksService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import io.swagger.annotations.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by Heyifan Cotter on 2018/10/17. 用户发布作品接口
 */


@RequestMapping("userWorksApi")
@Controller
public class userWorksInteface {
    @Autowired
    private ITagService tagService;

    @Autowired
    private IColumnTypeService columnTypeService;

    @Autowired
    private IWorksService worksService;

    @Autowired
    private ITagRelationService tagRelationService;

    @Autowired
    private IUserCommentService userCommentService;

    @Autowired
    private IUserFabulousService userFabulousService;


    /**
     * 发布作品
     *
     * @param works 前台出入作品
     * @return
     */
    @RequestMapping("postWorks")
    @ResponseBody
    ResultMsg postWorks(Works works) {

        Integer columnId = null;
        works.setCreateTime(new DateTime());
        EntityWrapper<ColumnType> columnTypeEntityWrapper = new EntityWrapper<>();
        columnTypeEntityWrapper.eq("name", "活动");
        List<ColumnType> columnTypes = columnTypeService.selectList(columnTypeEntityWrapper);
        works.setColumnId(columnTypes.get(0).getId());
        worksService.insert(works);

        if (works.getTagId() != "") {
            TagRelation tagRelation = new TagRelation();
            tagRelation.setCreateTime(new DateTime());
            tagRelation.setRelationId(works.getId());
            String tagArrId = works.getTagId();
            String[] heyifan = tagArrId.split(",");
            if (columnTypes.size() > 0) {
                columnId = columnTypes.get(0).getId();
            }

            for (int i = 0; i < heyifan.length; i++) {
                tagRelation.setRelationId(works.getId());
                tagRelation.setCreateTime(new DateTime());
                tagRelation.setCommonTypeId(columnId);
                tagRelation.setColumnId(Integer.parseInt(heyifan[i]));
                tagRelationService.insert(tagRelation);
            }

        }


        return ResultMsg.success("请求成功!", "code : 200", HttpStatus.OK);
    }


    /**
     * 获得作品下面的标签
     *
     * @return
     */
    @RequestMapping("getTagList")
    ResultMsg getTagList() {
        try {
            EntityWrapper<Tag> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("column_id", 17);
            List<Tag> tags = tagService.selectList(null);
            Map<String, Object> heyifan = new HashMap<>();
            if (heyifan.size() > 0) {
                for (int i = 0; i < tags.size(); i++) {
                    heyifan.put("id", tags.get(i).getId());
                    heyifan.put("name", tags.get(i).getName());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("请求失败!", "", HttpStatus.BAD_REQUEST);
        }


        return ResultMsg.success("请求成功!", "", HttpStatus.OK);
    }

    /**
     * 获取作品用户评论
     *
     * @param worksId
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取作品用户评论", notes = "获取通过作品id,获取作品的评论信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "columnId", value = "板块id", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
            @ApiImplicitParam(name = "worksId", value = "作品id", required = true),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{空,但是要注意看detail的返回内容}"))
    @RequestMapping(value = "getCommentByUser", method = RequestMethod.POST)
    @ResponseBody
    ResultMsg getCommentByUser(Integer columnId, Integer userId, Integer worksId) {
        Map<String, Object> map = new HashMap<>();
        try {
            Time time = new Time();
            List<commentVo> userComments = userCommentService.selectCommentByUserId(25, 321);
            /**
             *  设置时间和点赞数量
             */
            for (commentVo comment : userComments) {
                comment.setBeforetime(time.CalculateTime(comment.getBeforetime()));
                EntityWrapper<UserFabulous> entityWrapper = new EntityWrapper<>();
                entityWrapper.where("  user_id={0} and column_id={1}  and  works_id={2}", comment.getUserid(), columnId, worksId);
                List<UserFabulous> userFabulousList = userFabulousService.selectList(entityWrapper);
                comment.setLike(userFabulousList.size());
            }
            map.put("commentCount", userComments.size());
            map.put("commentList", userComments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("访问失败!", HttpStatus.BAD_REQUEST.toString(), "");
        }

        return ResultMsg.success("访问成功!", HttpStatus.OK.toString(), map);
    }

    @RequestMapping("qrCode")
    @ResponseBody
    String getQrCode() {
        String text = "http://192.168.1.28:8080/userWorksApi/staticHtml?userId=123";
        try {
            return QRCodeUtil.encode(text, new ClassPathResource("static/apk/log/log.png").getFile().getAbsolutePath(), "F:\\images\\", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
    @RequestMapping("staticHtml")
    String staticHtml(Integer userId ){
               System.out.println(userId);
        return  "newsDetail.html";
    }



}
