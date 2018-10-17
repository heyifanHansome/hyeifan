package com.stylefeng.guns.api.classroom;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.core.util.Time;
import com.stylefeng.guns.modular.classroom.service.IClassroomService;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.stylefeng.guns.modular.lijun.util.OSSClientUtil;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.http.HttpResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;


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




    @RequestMapping("/generatePoster")
    void generatePoster(String avater, String  qr, String description, HttpServletResponse response) {

        try {
            //目标文件
            File _file = new File("F:\\tempd\\1.jpg");
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
            g.drawImage(src_biao,  25,
                    642 + 52 + 213, 152, 151, null);
            //水印文件结束
            conn.disconnect();

            URL  avaterurl = new URL(avater);
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
            g.drawImage(sd, 55 , 521, 152, 151, null);
            //水印文件结束



            g.setColor(Color.RED);
            g.setFont(new Font(description, 1, 12));
            g.drawString(description, 253, 526 );
            g.dispose();

            FileOutputStream out = new FileOutputStream("F:/images/何一凡发送到.jpg");
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
            File file  = new File("F:/images/何一凡发送到.jpg");
            FileInputStream fileInputStream =  new FileInputStream(file);
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
























//        Map<String, MultipartFile> map=((MultipartHttpServletRequest)request).getFileMap();
//        MultipartFile avaterPicture = null;
//        MultipartFile qrPicture = null;
//        for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
//            String obj = i.next();
//            if(obj.equals("userPic")){
//                avaterPicture = (MultipartFile) map.get(obj);
//            }
//            if(obj.equals("qrPicture")){
//                qrPicture = (MultipartFile) map.get(obj);
//            }
//        }
//
//
//                try {
//                    InputStream tiltePictureStream = avaterPicture.getInputStream();
//                    //目标文件
//                    Image src = ImageIO.read(tiltePictureStream);
//                    int wideth = src.getWidth(null);
//                    int height = src.getHeight(null);
//                    BufferedImage image = new BufferedImage(wideth, height,
//                            BufferedImage.TYPE_INT_RGB);
//                    Graphics g = image.createGraphics();
//                    g.drawImage(src, 0, 0, wideth, height, null);
//
//                    InputStream thumnStream = qrPicture.getInputStream();
//                    Image src_biao = ImageIO.read(thumnStream);
//                    int wideth_biao = src_biao.getWidth(null);
//                    int height_biao = src_biao.getHeight(null);
//                    g.drawImage(src_biao, 536  +55 +25,
//                            642+52 +213, 152, 151, null);
//                    //水印文件结束
//                    g.dispose();
//
//
//                    FileOutputStream out = new FileOutputStream("F:/images/nm.jpg");
//                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//
//                    FileInputStream in = new FileInputStream("F:/images/dasfasd.jpg");
//                    IOUtils.copy(in, response.getOutputStream());

//                } catch (IOException e) {
//                    e.printStackTrace();
//                }



        }




//            try {
//                InputStream inputStream = multipartFile.getInputStream();
////            result=this.uploadFile2OSS(inputStream, name,goodsTypeId);
//            } catch (Exception e) {
//                throw new RuntimeException("图片上传失败");
//            }
//
//            try {
//                return "";
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
}
