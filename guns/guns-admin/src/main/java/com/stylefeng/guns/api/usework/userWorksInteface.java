package com.stylefeng.guns.api.usework;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.system.model.ColumnType;
import com.stylefeng.guns.modular.system.model.Tag;
import com.stylefeng.guns.modular.system.model.TagRelation;
import com.stylefeng.guns.modular.system.model.Works;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.tagRelation.service.ITagRelationService;
import com.stylefeng.guns.modular.works.service.IWorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Created by Heyifan Cotter on 2018/10/17.
 */
@RequestMapping("userWorksApi")
@RestController
public class userWorksInteface {
    @Autowired
    private ITagService tagService;

    @Autowired
    private IColumnTypeService columnTypeService;

    @Autowired
    private IWorksService worksService;

    @Autowired
    private ITagRelationService tagRelationService;

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



}
