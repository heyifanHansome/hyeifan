package com.stylefeng.guns.modular.lijun.web;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.stylefeng.guns.modular.lijun.util.FinalStaticString;
import com.stylefeng.guns.modular.lijun.util.OSSClientUtil;
import com.stylefeng.guns.modular.lijun.util.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/tool")
public class ToolController {

    @RequestMapping("/uploadFile")
    @ResponseBody
    public Object uploadFile(@RequestParam("file") MultipartFile file){
        OSSClientUtil ossClientUtil=new OSSClientUtil();
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("success", true);
        value.put("errorCode", 0);
        value.put("errorMsg", "");
        try {
            Map<String,Object>result=ossClientUtil.updateHead(file);
            String head = result.get("imgUrl").toString();
            value.put("data", head);
            value.put("object_name",result.get("ALi_ObjectName").toString());
        } catch (IOException e) {
            e.printStackTrace();
            value.put("success", false);
            value.put("errorCode", 200);
            value.put("errorMsg", "上传失败");
        }
        return value;
    }


    /**
     * 获取请求参数
     * @Title: getCallbackParams
     * @Description: TODO
     * @param @param request
     * @param @return
     * @param @throws Exception
     * @return Map<String,String>
     * @throws
     */
//    public Map<String, Object> getCallbackParams(HttpServletRequest request)
//            throws Exception {
//        InputStream inStream = request.getInputStream();
//        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int len = 0;
//        while ((len = inStream.read(buffer)) != -1) {
//            outSteam.write(buffer, 0, len);
//        }
//        outSteam.close();
//        inStream.close();
//        String result = new String(outSteam.toByteArray(), "utf-8");
//        return Tool.xmlToMap(result);
//    }
}
