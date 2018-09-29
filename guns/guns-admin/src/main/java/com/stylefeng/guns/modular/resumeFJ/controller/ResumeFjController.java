package com.stylefeng.guns.modular.resumeFJ.controller;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.FinalStaticString;
import com.stylefeng.guns.modular.lijun.util.Tool;
import com.stylefeng.guns.modular.system.model.UserResume;
import com.stylefeng.guns.modular.userResume.service.IUserResumeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.ResumeFj;
import com.stylefeng.guns.modular.resumeFJ.service.IResumeFjService;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * 简历附件控制器
 *
 * @author fengshuonan
 * @Date 2018-09-20 11:40:13
 */
@Controller
@RequestMapping("/resumeFj")
public class ResumeFjController extends BaseController {

    private String PREFIX = "/resumeFJ/resumeFj/";

    @Autowired
    private IResumeFjService resumeFjService;

    @Autowired
    private IUserResumeService userResumeService;
    /**
     * 跳转到简历附件首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "resumeFj.html";
    }

    /**
     * 跳转到添加简历附件
     */
    @RequestMapping("/resumeFj_add")
    public String resumeFjAdd() {
        return PREFIX + "resumeFj_add.html";
    }

    /**
     * 跳转到修改简历附件
     */
    @RequestMapping("/resumeFj_update/{resumeFjId}")
    public String resumeFjUpdate(@PathVariable Integer resumeFjId, Model model) {
        ResumeFj resumeFj = resumeFjService.selectById(resumeFjId);
        model.addAttribute("item",resumeFj);
        LogObjectHolder.me().set(resumeFj);
        return PREFIX + "resumeFj_edit.html";
    }

    /**
     * 获取简历附件列表,修改过后是根据简历ID,也就是resume_id来获取列表,不再根据搜索条件condition搜索
     * 用于"关于用户"→"用户简历管理"→每个用户简历的简历附件展示
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String resume_id,String condition) {
        ResumeFj fj=new ResumeFj();
        if(!Tool.isNull(resume_id))fj.setResumeId(resume_id);
        EntityWrapper<ResumeFj>wrapper=new EntityWrapper<>(fj);
        if(!Tool.isNull(condition))wrapper.like("name",condition);
        List<ResumeFj>fjs=resumeFjService.selectList(wrapper);
        for (ResumeFj resumeFj : fjs) {
            EntityWrapper ew_=new EntityWrapper(new UserResume());
            ew_.eq("id",resumeFj.getResumeId());
            List<UserResume>userResumeList=userResumeService.selectList(ew_);
            resumeFj.setResumeId(!Tool.listIsNull(userResumeList)?userResumeList.get(0).getName():"<span style='color:red;'>*对应简历已被删除*</span>");
        }
        return fjs;
    }

    /**
     * 新增简历附件
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ResumeFj resumeFj,String source,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
            ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET, old_object_name);
            ossClient.shutdown();
        }
        resumeFj.setCreateTime(new Date(System.currentTimeMillis()));
        resumeFjService.insert(resumeFj);
        if(!Tool.isNull(source))return resumeFj;
        return SUCCESS_TIP;
    }

    /**
     * 删除简历附件
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer resumeFjId) {
        ResumeFj resumeFj = resumeFjService.selectById(resumeFjId);
        OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
        ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET, resumeFj.getObject_name());
        ossClient.shutdown();
        resumeFjService.deleteById(resumeFjId);
        return SUCCESS_TIP;
    }

    /**
     * 修改简历附件
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ResumeFj resumeFj,String source,String old_object_name) {
        if(!Tool.isNull(old_object_name)){
            OSSClient ossClient = new OSSClient(FinalStaticString.ALI_OSS_ENDPOINT, FinalStaticString.ALI_OSS_ACCESS_ID, FinalStaticString.ALI_OSS_ACCESS_KEY);
            ossClient.deleteObject(FinalStaticString.ALI_OSS_BUCKET, old_object_name);
            ossClient.shutdown();
        }
        resumeFj.setUpdateTime(new Date(System.currentTimeMillis()));
        resumeFjService.updateById(resumeFj);
        if(!Tool.isNull(source))return resumeFj;
        return SUCCESS_TIP;
    }

    /**
     * 简历附件详情
     */
    @RequestMapping(value = "/detail/{resumeFjId}")
    @ResponseBody
    public Object detail(@PathVariable("resumeFjId") Integer resumeFjId) {
        return resumeFjService.selectById(resumeFjId);
    }
}
