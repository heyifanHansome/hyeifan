package com.stylefeng.guns.api.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.PublicMethod;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.QRCodeUtil;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.FSS;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.UserFabulous;
import com.stylefeng.guns.modular.userFabulous.service.IUserFabulousService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台API通用接口
 * Created by Heyifan Cotter on 2018/10/26.
 */
@Api(value = "前台通用Controller",tags = "前台通用接口")
@RequestMapping("commonApi")
@Controller
public class commonApi extends PublicMethod {

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

    /*-----------------------------------------------------------------------------------------------------*/
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号",required = true),
            @ApiImplicitParam(name="token",value = "登录码",required = true),
            @ApiImplicitParam(name="shareType",value = "分享类型",required = true),
            @ApiImplicitParam(name="shareObjectId",value = "分享对象ID",required = true)
    })
    @RequestMapping(value = "share",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg share(HttpServletResponse response,String shareType,String shareObjectId,String phone,String token) throws IOException {
        try{
//            if(Tool.isNull(shareObjectId))return ResultMsg.fail("缺少参数","shareObjectId",null);//不一定需要,根据传入shareType匹配,因为分享类型有可能是静态资源,而不是动态从数据库获取的
            if(Tool.isNull(phone))return ResultMsg.fail("缺少参数","phone",null);
            if(Tool.isNull(token))return ResultMsg.fail("缺少参数","token",null);
            if(Tool.isNull(shareType))return ResultMsg.fail("缺少参数","shareType",null);
            List<Map<String,Object>>users=getUser(phone,token);
            if(Tool.listIsNull(users))return ResultMsg.fail("请登录",null,null);
            Map<String,Object>shareMap=new HashMap<>();
            switch (shareType) {//分享类型判断
                case FSS.classroom://星厨课堂

                    break;
                case FSS.information://星厨资讯
                    break;
                case FSS.works://作品
                    break;
                case FSS.activity://活动
                    break;
                default:
                    return ResultMsg.fail("未知的分享类型","请查看并对应接口文档中的shareType字段说明",null);
            }
            // 读取原图片信息
            File srcImgFile = new File(FSS.SHARE_IMG_MODEL);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);

            //套入自有方法
            String text="水印内容";
            writeText(Color.black,new Font("微软雅黑", 3, 44),text,175,1291,g);

            // 输出图片
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            // 将图像输出到Servlet输出流中。
            ServletOutputStream sos = response.getOutputStream();
            ImageIO.write(bufImg, "jpeg", sos);
            sos.close();
            return ResultMsg.success("成功",null,null);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("系统错误",e.toString(),e);
        }
    }
//    private int getWatermarkLength(String waterMarkContent, Graphics2D g) {
//        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
//    }
    private void writeText(Color color,Font font,String text,int x,int y,Graphics2D g){
        g.setColor(color); //根据图片的背景设置水印颜色
        g.setFont(font);              //设置字体
        g.drawString(text, x, y);  //画出水印
        g.dispose();
    }
}
