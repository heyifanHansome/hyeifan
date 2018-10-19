package com.stylefeng.guns.modular.lijun.web;

import com.stylefeng.guns.modular.lijun.util.OSSClientUtil;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.systemSetting.service.ISettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/tool")
public class ToolController {
@Autowired
private ISettingService settingService;
    @RequestMapping("/uploadFile")
    @ResponseBody
    public Object uploadFile(@RequestParam("file") MultipartFile file){
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("success", true);
        value.put("errorCode", 0);
        value.put("errorMsg", "");
        Setting setting=settingService.selectById(1);
        if(setting==null){
            value.put("success", false);
            value.put("errorCode", 200);
            value.put("errorMsg", "系统设置'阿里云云存储'所有参数为空,请到'系统版块'→'系统设置'填写相关参数");
            return value;
        }
        System.err.println(setting);
        OSSClientUtil ossClientUtil=new OSSClientUtil(setting.getAliOssEndpoint(),setting.getAliOssAccessId(),setting.getAliOssAccessKey(),setting.getAliOssBucket(),setting.getAliOssFilePath());
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
