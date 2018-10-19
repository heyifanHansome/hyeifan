package com.stylefeng.guns.modular.ossFileInput;

/**
 * Created by Heyifan Cotter on 2018/9/26.
 */

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.OSSClientUtil;
import com.stylefeng.guns.modular.lijun.util.SettingConfiguration;
import com.stylefeng.guns.modular.picture.service.IPictureService;
import com.stylefeng.guns.modular.system.dao.PictureMapper;
import com.stylefeng.guns.modular.system.model.Picture;
import com.stylefeng.guns.modular.system.model.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author Heyifan
 * @version 1.0
 * @date 2018年8月15日 22:12:12
 * @Descrption:图片上传工具
 */

@Controller
@RequestMapping("img")
public class ImgUploadController {
@Autowired
private SettingConfiguration settingConfiguration;
    @Autowired
    public PictureMapper pictureMapper;
    @Autowired
    public IPictureService pictureService;

    private OSSClient ossClient;

//    private String endpoint = FSS.ALI_OSS_ENDPOINT;
//    // accessKey
//    private String accessKeyId = FSS.ALI_OSS_ACCESS_ID;
//    private String accessKeySecret = FSS.ALI_OSS_ACCESS_KEY;;
//    //空间
//    private String bucketName = FSS.ALI_OSS_BUCKET;
    //文件存储目录
    private String filedir = "data/";

    private String frist = "https://cheshi654321.oss-cn-beijing.aliyuncs.com/";



    /**
     * 文件上传删除方法
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/imgDeleteMul")
    public ResponseEntity<?> imgDeleteMul(HttpServletRequest request, HttpServletResponse response) {

        ResultMsg resultMsg = new ResultMsg();
        String id = (String) request.getParameter("key");//获取图片id
        Setting setting=settingConfiguration.getSetting();
        OSSClient ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
        int index   = id.indexOf("/data");
       String newString =  id.substring(index+1);
        ossClient.deleteObject(setting.getAliOssBucket(), newString);

        ossClient.shutdown();

        EntityWrapper<Picture> entityWrapper = new EntityWrapper();
        entityWrapper.like("oss_object_name", id );




        List<Picture> pictures =pictureService.selectList(entityWrapper);


//
//        Integer pid = Integer.parseInt(id);
//        String uploadPath = PropertiesUtil.getValueByKey("imgUploadPath");
//        Picture picture = pictureService.selectById(id);
        try {
////            if (picture != null) {
////                if (picture.getPicturename() != null && !"".equals(picture.getPicturename())) {
////                    File file = new File(uploadPath + picture.getPicturename() + picture.getSuffixname());//删除文件
////                    file.delete();
////                    pictureService.deleteById(pid);
////                    resultMsg = ResultMsg.success("操作成功", "操作成功", "");
////                }
//            }
            pictureService.deleteById(pictures.get(0).getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ResultMsg>(ResultMsg.fail("系统错误", e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ResultMsg>(resultMsg, HttpStatus.OK);
    }


    /*文件上传的方法*/
    @PostMapping(value = "/imgUploadMul")
    @ResponseBody
    public String imgUploadMul(HttpServletRequest request, HttpServletResponse response, String goodsTypeId) {
        OSSClientUtil ossClientUtil=new OSSClientUtil();
        Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
        MultipartFile multipartFile = null;
        for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            multipartFile = (MultipartFile) map.get(obj);
        }

//        ossClientUtil.uploadImg2Oss(multipartFile);
        String originalFilename = multipartFile.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        Map<String,Object>result=new HashMap<>();

        try {
            InputStream inputStream = multipartFile.getInputStream();
            result=this.uploadFile2OSS(inputStream, name,goodsTypeId);
        } catch (Exception e) {
            throw new RuntimeException("图片上传失败");
        }

        try {
            return "";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public Map<String,Object> uploadFile2OSS(InputStream instream, String fileName,String goodsTypeId) {
        Map<String,Object>result=new HashMap<>();
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            //ObjectName为filedir + fileName,这个想办法传回去,让数据库记录起来,在删除记录的时候,还需要把ObjectName传给阿里云,删除服务器上资源
            Setting setting=settingConfiguration.getSetting();
            ossClient = new OSSClient(setting.getAliOssEndpoint(), setting.getAliOssAccessId(), setting.getAliOssAccessKey());
//            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            PutObjectResult putResult = ossClient.putObject(setting.getAliOssBucket(), filedir + fileName, instream, objectMetadata);

            Picture picture = new Picture();
            picture.setBaseId(goodsTypeId);
            picture.setOssObjectName(frist+filedir + fileName);
            pictureService.insert(picture);

        } catch (IOException e) {
           e.printStackTrace();
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return ret;
        return result;
    }




    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equalsIgnoreCase(".mp4")) {
            return "video/mp4";
        }
        return "image/jpeg";
    }


}
