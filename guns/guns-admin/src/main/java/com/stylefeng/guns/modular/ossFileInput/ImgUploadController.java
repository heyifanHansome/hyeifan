package com.stylefeng.guns.modular.ossFileInput;
/**
 * Created by Heyifan Cotter on 2018/9/26.
 */
import com.stylefeng.guns.core.shiro.ShiroUser;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.FinalStaticString;
import com.stylefeng.guns.modular.picture.service.IPictureService;
import com.stylefeng.guns.modular.system.dao.PictureMapper;
import com.stylefeng.guns.modular.system.model.Picture;
import org.apache.shiro.SecurityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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
    public PictureMapper pictureMapper;
    @Autowired
    public IPictureService pictureService;


    @PostMapping("/new")
    public String imgUpdate(MultipartFile file) {
        String uploadPath = FinalStaticString.FILEPATHIMG;
        if (file.isEmpty()) {
            return "error";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 这里我使用随机字符串来重新命名图片
        fileName = Calendar.getInstance().getTimeInMillis() + UUID.randomUUID().toString() + suffixName;
        // 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
        File dest = new File(uploadPath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            // 创建图片对象
            Picture picture = new Picture();
            picture.setPicturename(fileName.replace(suffixName, ""));// 图片名称
            picture.setRelativepath(uploadPath);// 相对路径
            picture.setServerpath(uploadPath);// 服务器路径
            picture.setAbsolutepath(uploadPath);// 绝对路径
            picture.setType("");// 图片类型
            picture.setSuffixname(suffixName);// 图片后缀名

            pictureService.insert(picture);
            // url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
            String config = "{\"state\": \"SUCCESS\"," + "\"url\": \"" + "/img/getImage/" + picture.getId() + "\","
                    + "\"pictureId\": \"" + picture.getId() + "\"," + "\"original\": \"" + fileName + "\"}";

            return config;

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }


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
        Integer pid = Integer.parseInt(id);
        String uploadPath = FinalStaticString.FILEPATHIMG;//图片保存路径
        Picture picture = pictureService.selectById(id);
        try {
            if (picture != null) {
                if (picture.getPicturename() != null && !"".equals(picture.getPicturename())) {
                    File file = new File(uploadPath + picture.getPicturename() + picture.getSuffixname());//删除文件
                    file.delete();
                    pictureService.deleteById(pid);
                    resultMsg = ResultMsg.success("操作成功", "操作成功", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ResultMsg>(ResultMsg.fail("系统错误", e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ResultMsg>(resultMsg, HttpStatus.OK);
    }

    @PostMapping(value = "/imgUploadMul")
    @ResponseBody
    public String imgUploadMul(HttpServletRequest request, HttpServletResponse response, String goodsTypeId) {

        Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();

        MultipartFile multipartFile = null;
        for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            multipartFile = (MultipartFile) map.get(obj);

        }
        String uploadPath = FinalStaticString.FILEPATHIMG;
        if (multipartFile.isEmpty()) {
            return "error";
        }
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 这里我使用随机字符串来重新命名图片
        fileName = Calendar.getInstance().getTimeInMillis() + UUID.randomUUID().toString() + suffixName;
        // 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
        File dest = new File(uploadPath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(dest);
            //创建图片对象
            Picture picture = new Picture();

            picture.setCreateTime(new DateTime());
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            picture.setCreateBy(shiroUser.getName());
            picture.setBaseId(goodsTypeId);
            picture.setPicturename(fileName.replace(suffixName, ""));//图片名称
            picture.setRelativepath(uploadPath);//相对路径
            picture.setServerpath(uploadPath);//服务器路径
            picture.setAbsolutepath(uploadPath);//绝对路径
            picture.setType("");//图片类型
            picture.setSuffixname(suffixName);//图片后缀名

            pictureService.insert(picture);
            //url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
            String config = "{\"state\": \"SUCCESS\"," +
                    "\"url\": \"" + "/img/getImage/" + picture.getId() + "\"," +
                    "\"pictureId\": \"" + picture.getId() + "\"," +
                    "\"original\": \"" + fileName + "\"}";
            return config;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片方法
     * @param id
     * @param response
     */
    @RequestMapping(value = "getImage/{id}")
    @ResponseBody
    public void getImageById(@PathVariable("id") String id, HttpServletResponse response) {
        String imgpath = "";
        if (id != null && !"".equals(id)) {
            Picture p = pictureService.selectById(id);// 获取图片对象
            imgpath = p.getAbsolutepath() + p.getPicturename() + p.getSuffixname();//
            File file = new File(imgpath);
//            response.setContentType("video/mpeg4");
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                try {
                    IOUtils.copy(in, response.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//
//            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
//            response.setContentType("multipart/form-data");
//            //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
//            ServletOutputStream out;
//            try {
//                FileInputStream inputStream = new FileInputStream(file);
//                //3.通过response获取ServletOutputStream对象(out)
//                out = response.getOutputStream();
//                int b = 0;
//                byte[] buffer = new byte[512];
//                while (b != -1){
//                    b = inputStream.read(buffer);
//                    //4.写到输出流(out)中
//                    out.write(buffer,0,b);
//                }
//                inputStream.close();
//                out.close();
//                out.flush();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }

    /**
     * 上传文件
     *
     * @param request
     * @param response
     * @param file     上传的文件，支持多文件
     * @throws Exception
     */
    @RequestMapping("/fileInsert")
    public void insert(HttpServletRequest request, HttpServletResponse response
            , @RequestParam("reportFile") MultipartFile[] file) throws Exception {
        if (file != null && file.length > 0) {
            try {
                for (int i = 0; i < file.length; i++) {
                    if (!file[i].isEmpty()) {
                        imgUpdate(file[i]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    @RequestMapping(value = "getCarImage/{id}")
    @ResponseBody
    public ResponseEntity<?> getCarImageById(@PathVariable("id") String id, HttpServletResponse response) {
        ResultMsg resultMsg = new ResultMsg();
        String imgpath = "";
        if (id != null && !"".equals(id)) {
            Picture p = pictureService.selectById(id);// 获取图片对象
            imgpath = p.getAbsolutepath() + p.getPicturename() + p.getSuffixname();//
            //File file = new File(imgpath);
            response.setContentType("image/jpg");
		/*	InputStream in = null;
			try {
				in = new FileInputStream(file);
				try {
					IOUtils.copy(in, response.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}*/
            resultMsg.setData(imgpath);
        }
        return new ResponseEntity<ResultMsg>(resultMsg, HttpStatus.OK);
    }

    /**
     * 订单评论图片上传处理
     *
     * @param file
     * @return
     */
    @PostMapping("/commentsImg")
    @ResponseBody
    public ResultMsg commentsImgUpdate(String commentsId, MultipartFile file) {
        String uploadPath = FinalStaticString.FILEPATHIMG;
        if (file.isEmpty()) {
            return ResultMsg.success("", null, "false");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 这里我使用随机字符串来重新命名图片
        fileName = Calendar.getInstance().getTimeInMillis() + UUID.randomUUID().toString() + suffixName;
        // 这里的路径为Nginx的代理路径，这里是/data/images/xxx.png
        File dest = new File(uploadPath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            // 创建图片对象
            Picture picture = new Picture();
            picture.setBaseId(commentsId);
            picture.setPicturename(fileName.replace(suffixName, ""));// 图片名称
            picture.setRelativepath(uploadPath);// 相对路径
            picture.setServerpath(uploadPath);// 服务器路径
            picture.setAbsolutepath(uploadPath);// 绝对路径
            picture.setType("");// 图片类型
            picture.setSuffixname(suffixName);// 图片后缀名

            pictureService.insert(picture);
            // url的值为图片的实际访问地址 这里我用了Nginx代理，访问的路径是http://localhost/xxx.png
//			String config = "{\"state\": \"SUCCESS\"," + "\"url\": \"" + "/img/getImage/" + picture.getId() + "\","
//					+ "\"pictureId\": \"" + picture.getId() + "\"," + "\"original\": \"" + fileName + "\"}";
//			System.out.println(config);
            return ResultMsg.success("", null, "true");

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultMsg.success("", null, "false");
    }
}
