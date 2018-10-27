package com.stylefeng.guns.api.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.QRCodeUtil;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.system.model.UserFabulous;
import com.stylefeng.guns.modular.userFabulous.service.IUserFabulousService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
import java.util.List;

/**
 * 通用接口
 * Created by Heyifan Cotter on 2018/10/26.
 */
@RequestMapping("commonApi")
public class commonApi {

    @Autowired
    private IUserFabulousService userFabulousService;

    /**
     * 点赞接口
     *
     * @param userFabulous 点赞表
     * @return
     */
    @ApiOperation(value = "点赞取消赞", notes = "用来取消点赞")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "UserFabulous", value = "点赞对象", required = true)
//            @ApiImplicitParam(name = "userId", value = "点赞用户id", required = true),
//            @ApiImplicitParam(name = "columnId", value = "点赞板块id", required = true),
//            @ApiImplicitParam(name = "worksId", value = "具体id", required = true)
    )
    @RequestMapping(value = "clickLike", method = RequestMethod.POST)
    @ResponseBody
    ResultMsg clickLike(UserFabulous userFabulous, Integer userId) {
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

    /**
     * 测试生成带图片的二维码
     * @param inviteCode
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/heyifan")
    public String GenerateInviteQrcode(String inviteCode, String file) throws Exception {
        //生成带logo 的二维码
        String text = "https://blog.csdn.net/m0_37172806/article/details/78193126";
        return QRCodeUtil.encode(text, new ClassPathResource("static/apk/log/log.png").getFile().getAbsolutePath(), "F:\\images\\", true);
    }

    /**
     *  生成海报方法
     * @param avater
     * @param qr
     * @param description
     * @param response
     */
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
            g.drawImage(src_biao, 25, 642 + 52 + 213, 152, 151, null);
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





}
