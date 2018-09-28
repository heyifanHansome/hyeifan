package com.stylefeng.guns.modular.oss;

/**
 * Created by Heyifan Cotter on 2018/9/27.
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.UploadFileRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.mts.model.v20140618.AddTemplateRequest;
import com.aliyuncs.mts.model.v20140618.AddTemplateResponse;
import com.aliyuncs.mts.model.v20140618.DeleteTemplateRequest;
import com.aliyuncs.mts.model.v20140618.QueryJobListRequest;
import com.aliyuncs.mts.model.v20140618.QueryJobListResponse;
import com.aliyuncs.mts.model.v20140618.SearchPipelineRequest;
import com.aliyuncs.mts.model.v20140618.SearchPipelineResponse;
import com.aliyuncs.mts.model.v20140618.SubmitJobsRequest;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse.JobResult;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse.JobResult.Job;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse.JobResult.Job.Output;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse.JobResult.Job.Output.OutputFile;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
public class VedioUtil {
    /**
     * 断点上传文件
     *
     * @param endpoint
     * @param accessKeyId
     * @param accessKeySecret
     * @param fileurl         文件路径
     * @param bucket          传入的bucket名称
     * @param filename        文件名称
     * @param location        传入的bucket服务区
     * @throws Throwable
     */
    public String uploadFile(String endpoint, String accessKeyId, String accessKeySecret, String fileurl, String bucket, String filename, String location, String templateId) throws Throwable {
//        filename = ToolUtil.getRandomString(23);
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 设置断点续传请求
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucket, filename);
        // 指定上传的本地文件
        uploadFileRequest.setUploadFile(fileurl);
        // 指定上传并发线程数
        uploadFileRequest.setTaskNum(5);
        // 指定上传的分片大小
        uploadFileRequest.setPartSize(1 * 1024 * 1024);
        // 开启断点续传
        uploadFileRequest.setEnableCheckpoint(true);
        System.out.println("开始上传视频.....");
        // 断点续传上传
        ossClient.uploadFile(uploadFileRequest);
        System.out.println("上传成功！");
        System.out.println("开始转码...");
        String url = transcoding(bucket, location, filename, accessKeyId, accessKeySecret, templateId);
        System.out.println("转码后播放地址：" + url);
        //删除指定对象
        ossClient.deleteObject(bucket, filename);
        // 关闭client
        ossClient.shutdown();
        return url;
    }

    /**
     * 视频转码
     *
     * @param bucket          输入bucket名称
     * @param location        输入服务区
     * @param filename        文件名
     * @param accessKeyId
     * @param accessKeySecret
     * @return 转码后播放地址
     * @throws ClientException
     */
    public String transcoding(String bucket, String location, String filename, String accessKeyId, String accessKeySecret, String templateId) throws ClientException {
        System.out.println(bucket + "--" + location + "--" + templateId);
        String url = "";
        if (location.equals("oss-cn-beijing")) {
            //华北2
            DefaultProfile.addEndpoint("cn-beijing",
                    "cn-beijing",
                    "Mts",
                    "mts.cn-beijing.aliyuncs.com");
        } else if (location.equals("oss-cn-shanghai")) {
            //华东2
            DefaultProfile.addEndpoint("cn-shanghai",
                    "cn-shanghai",
                    "Mts",
                    "mts.cn-shanghai.aliyuncs.com");
        } else if (location.equals("oss-cn-hangzhou")) {
            //华东1
            DefaultProfile.addEndpoint("cn-hangzhou",
                    "cn-hangzhou",
                    "Mts",
                    "mts.cn-hangzhou.aliyuncs.com");
        } else if (location.equals("oss-cn-shenzhen")) {
            //华南1
            DefaultProfile.addEndpoint("cn-shenzhen",
                    "cn-shenzhen",
                    "Mts",
                    "mts.cn-shenzhen.aliyuncs.com");
        }

        //开始转码
        SubmitJobsRequest submitjobs = new SubmitJobsRequest();
        String input = "{\"Bucket\":\"" + bucket + "\",\"Location\":\"" + location + "\",\"Object\":\"" + filename + "\"}";
        String outputs = "[{\"OutputObject\":\"_" + filename + "\",\"TemplateId\":\"" + templateId + "\"}]";
        String outputbucket = bucket;
        String OutputLocation = location;
        submitjobs.setOutputLocation(OutputLocation);
        submitjobs.setInput(input);
        submitjobs.setOutputs(outputs);
        submitjobs.setOutputBucket(outputbucket);
        //创建IClientProfile
        IClientProfile profile = DefaultProfile.getProfile(location.substring(4), accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        try {
            //获取管道
            SearchPipelineRequest searchPipeline = new SearchPipelineRequest();
            SearchPipelineResponse sarchPipelineResponse = client.getAcsResponse(searchPipeline);
            String pipelineid = sarchPipelineResponse.getPipelineList().get(0).getId();
            System.out.println("管道id=" + pipelineid);
            //给管道赋值
            submitjobs.setPipelineId(pipelineid);
            SubmitJobsResponse response = client.getAcsResponse(submitjobs);
            List<JobResult> list = response.getJobResultList();
            JobResult jobResult = list.get(0);
            Job job = jobResult.getJob();
            Output output = job.getOutput();
            OutputFile outputfile = output.getOutputFile();
            url = "http://" + outputfile.getBucket() + "." + outputfile.getLocation() + ".aliyuncs.com/" + outputfile.getObject();
            String jobid = job.getJobId();
            System.out.println("转码任务id=" + jobid);
            QueryJobListRequest queryjoblist = new QueryJobListRequest();
            queryjoblist.setJobIds(jobid);
            for (int i = 0; i < 2; i = 0) {
                QueryJobListResponse queryresponse = client.getAcsResponse(queryjoblist);
                String state = queryresponse.getJobList().get(0).getState();
                if (state.equals("Transcoding") || state.equals("Submitted")) {
                    Thread thread = Thread.currentThread();
                    thread.sleep(5000);//暂停5秒后程序继续执行
                } else if (state.equals("TranscodeFail") || state.equals("TranscodeCancelled")) {
                    url = "";
                } else {
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            url = "";
        }
        return url;
    }

    /**
     * 上传模板
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param name            模板名称
     * @param container       模板容器
     * @param video           视频流配置
     * @param audio           音频流配置
     * @return
     * @throws ClientException
     */
    public static String addTemplate(String location, String accessKeyId, String accessKeySecret, String name, String container, String video, String audio) throws ClientException {
        String templateid = "";
        if (location.equals("oss-cn-beijing")) {
            //华北2
            DefaultProfile.addEndpoint("cn-beijing",
                    "cn-beijing",
                    "Mts",
                    "mts.cn-beijing.aliyuncs.com");
        } else if (location.equals("oss-cn-shanghai")) {
            //华东2
            DefaultProfile.addEndpoint("cn-shanghai",
                    "cn-shanghai",
                    "Mts",
                    "mts.cn-shanghai.aliyuncs.com");
        } else if (location.equals("oss-cn-hangzhou")) {
            //华东1
            DefaultProfile.addEndpoint("cn-hangzhou",
                    "cn-hangzhou",
                    "Mts",
                    "mts.cn-hangzhou.aliyuncs.com");
        } else if (location.equals("oss-cn-shenzhen")) {
            //华南1
            DefaultProfile.addEndpoint("cn-shenzhen",
                    "cn-shenzhen",
                    "Mts",
                    "mts.cn-shenzhen.aliyuncs.com");
        }

        AddTemplateRequest template = new AddTemplateRequest();
        template.setName(name);
        if (!container.equals("")) {
            template.setContainer(container);
        }
        if (!video.equals("")) {
            template.setVideo(video);
        }
        if (!audio.equals("")) {
            template.setAudio(audio);
        }
        //创建IClientProfile
        IClientProfile profile = DefaultProfile.getProfile(location.substring(4), accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        try {
            AddTemplateResponse templateResponse = client.getAcsResponse(template);
            templateid = templateResponse.getTemplate().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return templateid;
    }

    /**
     * 删除模板
     *
     * @param location
     * @param accessKeyId
     * @param accessKeySecret
     * @param template_id
     * @throws ClientException
     */
    public static void deleteTemplate(String location, String accessKeyId, String accessKeySecret, String template_id) throws ClientException {
        if (location.equals("oss-cn-beijing")) {
            //华北2
            DefaultProfile.addEndpoint("cn-beijing",
                    "cn-beijing",
                    "Mts",
                    "mts.cn-beijing.aliyuncs.com");
        } else if (location.equals("oss-cn-shanghai")) {
            //华东2
            DefaultProfile.addEndpoint("cn-shanghai",
                    "cn-shanghai",
                    "Mts",
                    "mts.cn-shanghai.aliyuncs.com");
        } else if (location.equals("oss-cn-hangzhou")) {
            //华东1
            DefaultProfile.addEndpoint("cn-hangzhou",
                    "cn-hangzhou",
                    "Mts",
                    "mts.cn-hangzhou.aliyuncs.com");
        } else if (location.equals("oss-cn-shenzhen")) {
            //华南1
            DefaultProfile.addEndpoint("cn-shenzhen",
                    "cn-shenzhen",
                    "Mts",
                    "mts.cn-shenzhen.aliyuncs.com");
        }
        DeleteTemplateRequest deleterequest = new DeleteTemplateRequest();
        deleterequest.setTemplateId(template_id);
        //创建IClientProfile
        IClientProfile profile = DefaultProfile.getProfile(location.substring(4), accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        try {
            client.getAcsResponse(deleterequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除视频
     *
     * @param endpoint
     * @param accessKeyId
     * @param accessKeySecret
     * @param bucket          传入的bucket名称
     * @param url             视频播放地址
     */
    public void deleteVideo(String endpoint, String accessKeyId, String accessKeySecret, String bucket, String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteBucketCname(bucket, filename);
        // 关闭client
        ossClient.shutdown();
    }

    //直接使用MultipartFile上传到阿里云服务器  返回转码后的url
    public String getOSSUrl(MultipartFile file, String name, String template_id) throws IOException {

     /*  Parament endpoint=paramentService.findByName("endpoint");
        Parament accessKeyId=paramentService.findByName("accessKeyId");
        Parament accessKeySecret=paramentService.findByName("accessKeySecret");
        Parament bucket=paramentService.findByName("bucket");
        Parament location=paramentService.findByName("location");*/
        OSSClient ossClient = new OSSClient("http://oss-cn-beijing.aliyuncs.com", "LTAIPAfHfcl2Ycjs", "o253yE8pCkLxA08nAcVlEYPI1a6sev");
        InputStream inputStream = file.getInputStream();
        ossClient.putObject("heyifan", name, inputStream);
        VedioUtil vedioUtil = new VedioUtil();
        String url = "";
        try {
            url = vedioUtil.transcoding("heyifan", "oss-cn-beijing", name, "LTAIPAfHfcl2Ycjs", "o253yE8pCkLxA08nAcVlEYPI1a6sev", template_id);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static void main(String[] args) throws Throwable {
        VedioUtil a = new VedioUtil();
        a.uploadFile("http://oss-cn-beijing.aliyuncs.com", "LTAIPAfHfcl2Ycjs", "o253yE8pCkLxA08nAcVlEYPI1a6sev", /*"F:\\springcloudteacker\\05 Docker+Kubernetes(k8s)微服务容器化实践(高清版)\\第2章 微服务带来的问题及解决方案分析\\2-1 微服务架构带来的问题.mp4"*/"C:\\Users\\Administrator\\Desktop\\images\\1.png", "cheshi654321", "sunshineFlowers", "oss-cn-beijing", "92e9239db5f468296b095d69706e09ed");
    }
}
