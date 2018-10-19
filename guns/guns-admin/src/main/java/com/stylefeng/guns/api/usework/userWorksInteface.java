package com.stylefeng.guns.api.usework;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.QRCodeUtil;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.core.util.Time;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.vo.commentVo;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.tagRelation.service.ITagRelationService;
import com.stylefeng.guns.modular.userCommentModel.service.IUserCommentService;
import com.stylefeng.guns.modular.userFabulous.service.IUserFabulousService;
import com.stylefeng.guns.modular.works.service.IWorksService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping("/heyifan")
    public String GenerateInviteQrcode(String inviteCode, String file) throws Exception {
        //生成带logo 的二维码
        String text = "https://blog.csdn.net/m0_37172806/article/details/78193126";
        return QRCodeUtil.encode(text, new ClassPathResource("static/apk/log/log.png").getFile().getAbsolutePath(), "F:\\images\\", true);
    }


    @RequestMapping("/generatePoster")
    void generatePoster(String avater, String qr, String description, HttpServletResponse response) {

        try {
            //目标文件
            File _file = new File("F:\\tempd\\1.jpg");
            System.out.println(_file);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);

            URL url = new URL(qr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();

            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            //水印文件
//            File _filebiao = new File(pressImg);
//            System.out.println(_filebiao.exists());
            Image src_biao = ImageIO.read(inStream);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(src_biao, 25,
                    642 + 52 + 213, 152, 151, null);
            //水印文件结束
            conn.disconnect();

            URL avaterurl = new URL(avater);
            HttpURLConnection avaterurlconn = (HttpURLConnection) avaterurl.openConnection();
            avaterurlconn.setRequestMethod("GET");
            //超时响应时间为5秒
            avaterurlconn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream dinStream = avaterurlconn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            //水印文件
//            File _filebiao = new File(pressImg);
//            System.out.println(_filebiao.exists());
            Image sd = ImageIO.read(dinStream);
            int wq = src_biao.getWidth(null);
            int qew = src_biao.getHeight(null);
            g.drawImage(sd, 55, 521, 152, 151, null);
            //水印文件结束
            g.setColor(Color.RED);
            g.setFont(new Font(description, 1, 12));
            g.drawString(description, 253, 526);
            g.dispose();

            FileOutputStream out = new FileOutputStream("F:/images/何一凡发送到.jpg");
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
            File file = new File("F:/images/何一凡发送到.jpg");
            FileInputStream fileInputStream = new FileInputStream(file);
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取作品用户评论
     *
     * @param worksId
     * @param userId
     * @return
     */
    @RequestMapping("getCommentByUser")
    @ResponseBody
    ResultMsg getCommentByUser(Integer worksId, Integer userId) {
        Map<String, Object> map = new HashMap<>();
        try {
            Time time = new Time();
            List<commentVo> userComments = userCommentService.selectCommentByUserId(25, 321);
            for (commentVo comment : userComments) {
                comment.setBeforetime(time.CalculateTime(comment.getBeforetime()));
            }
            map.put("commentCount", userComments.size());
            map.put("commentList", userComments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("访问失败!", HttpStatus.BAD_REQUEST.toString(), "");
        }

        return ResultMsg.success("访问成功!", HttpStatus.OK.toString(), map);
    }


    /**
     * 点赞取消赞公用接口
     *
     * @param userFabulous 点赞表
     * @return
     */
    @RequestMapping("clickLike")
    @ResponseBody
    ResultMsg clickLike(UserFabulous userFabulous) {

        try {
            EntityWrapper<UserFabulous> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("user_id ={0} and column_id={1} and works_id={2}", userFabulous.getUserId(), userFabulous.getColumnId(), userFabulous.getWorksId());
            List<UserFabulous> userFabulous1 = userFabulousService.selectList(entityWrapper);
            if (userFabulous1.size() == 0) {
                userFabulous.setCreateTime(new DateTime());
                userFabulousService.insert(userFabulous);
                return ResultMsg.success("点赞成功!", HttpStatus.OK.toString(), "yes");
            } else {
                userFabulousService.deleteById(userFabulous1.get(0).getId());
                return ResultMsg.success("取消点赞成功!!", HttpStatus.OK.toString(), "no");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail("服务器异常,请联系管理员!", HttpStatus.BAD_REQUEST.toString(), "no");
        }

    }
}
