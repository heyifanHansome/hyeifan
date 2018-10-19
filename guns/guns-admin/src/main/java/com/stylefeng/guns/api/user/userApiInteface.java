package com.stylefeng.guns.api.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.*;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.model.UserInfo;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 前台用户登录接口实现类
 */
@RestController
@RequestMapping("userApi")
public class userApiInteface {
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private SettingConfiguration settingConfiguration;
    @Autowired
    private ITagService tagService;
    @Autowired
    private Dao dao;
    @ApiOperation(value = "获取手机验证码",notes = "根据手机号生成验证码以短信形式发送,返回的内容里会有成功与否的消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{空,但是要注意看detail的返回内容}"))
    @RequestMapping(value="getVerificationCode",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getVerificationCode(String phone){
        int randomCode= Tool.getRandomNum();
        try {
            Setting setting=settingConfiguration.getSetting();
            String str=new DuanXin_LiJun(setting.getYp_appkey(),"模版ID").tplSendSms(String.valueOf(randomCode),phone);
            Map<String,Object>result= JSONObject.fromObject(str);
            if(!Tool.isNull(result.get("code"))){
                if("0".equals(result.get("code"))){
                    ((HttpSession)Tool.getRequest_Response_Session()[2]).setAttribute(FSS.LOGIN_CODE,randomCode);
                    return ResultMsg.success("发送成功!",result.toString(),null);
                }else{
                    return ResultMsg.fail("发送失败",result.toString(),null);
                }
            }else{
                return ResultMsg.fail("发送失败",result.toString(),null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMsg.fail("发送失败",e.toString(),null);
        }
    }

    @ApiOperation(value = "用户登录",notes = "根据用户手机号和验证码登录并且记录用户信息,然后将更新后的用户信息返回")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号",required = true),
            @ApiImplicitParam(name="code",value = "验证码",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "birthday:生日," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "createtime:注册时间," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "sex:性别;(0:其他;1:男;2:女)," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "avatar:头像," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "apiToken:登录状态的TOKEN," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "phone:手机号," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "name:昵称," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "id:用户ID," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "email:邮箱," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "}"))
    @RequestMapping(value = "userLogin",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg userLogin(String phone,String code){
        try {
            Object session_code=((HttpSession)Tool.getRequest_Response_Session()[2]).getAttribute(FSS.LOGIN_CODE);
            if(Tool.isNull(phone))return ResultMsg.fail("请输入手机号",null,null);
            if(!"localhost".equals(Tool.getDomain())&&!"192.168.1.21".equals(Tool.getDomain())&&!"192.168.1.28".equals(Tool.getDomain())){
                if(Tool.isNull(session_code))return ResultMsg.fail("请先获取验证码",null,null);
                if(Tool.isNull(code))return ResultMsg.fail("请输入验证码",null,null);
                if(!session_code.equals(code))return ResultMsg.fail("验证码错误",null,null);
            }
            EntityWrapper<UserApi> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("phone",phone);
            List<UserApi>  userApis = userApiService.selectList(entityWrapper);
            String apiToken=UuidUtil.get32UUID();
            if(!Tool.listIsNull(userApis)){
                UserApi userApi = userApis.get(0);
                Map<String,Object>userApiMap= JavaBeanUtil.convertBeanToMap(userApi);
                UserInfo info=new UserInfo();
                info.setApiToken(apiToken);
                EntityWrapper<UserInfo>userInfoEntityWrapper=new EntityWrapper<>();
                userInfoEntityWrapper.eq("user_id",userApi.getId());
                userInfoService.update(info,userInfoEntityWrapper);
                userApiMap.put("apiToken",apiToken);
                Tool.removeMapParmeByKey(userApiMap,new String[]{"salt","password","object_name","verification_code","account","verificationCode","updatetime"});
                return ResultMsg.success("登录成功",null,userApiMap);
            }else{
                UserApi userApi=new UserApi();
                userApi.setPhone(phone);
                userApi.setCreatetime(new Date());
                userApi.setSex("0");
                userApi.setName("新用户"+userApiService.selectCount(new EntityWrapper<>()));
                userApi.setAvatar("http://cheshi654321.oss-cn-beijing.aliyuncs.com/data/1539839811974.png?Expires=1855199802&OSSAccessKeyId=LTAIPAfHfcl2Ycjs&Signature=Hz3ynjO9M6mf2x%2FT0%2BEPfJOjbo8%3D");
                userApi.setObject_name("data/1539839811974.png");
                userApiService.insert(userApi);
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(userApi.getId());
                userInfo.setApiToken(apiToken);
                userInfo.setCredits(0);
                userInfo.setMoney(0);
                userInfo.setLoginIp("192.168.0.0.1");
                userInfo.setCreateTime(new DateTime());
//                userInfo.setRealName(userApi.getName());//实名认证后才有,根据这个字段是否为空判断用户进行了实名认证没
                userInfo.setJoinClub(0);
                userInfo.setAppointment(0);
                userInfo.setEnlightening(0);
                userInfoService.insert(userInfo);
                Map<String,Object>userApiMap= JavaBeanUtil.convertBeanToMap(userApi);
                userApiMap.put("apiToken",apiToken);
                Tool.removeMapParmeByKey(userApiMap,new String[]{"salt","password","object_name","verification_code","account"});
                return ResultMsg.success("登录成功",null,userApiMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultMsg.fail("系统错误",e.toString(),null);
        }
    }

    @ApiOperation(value = "用户实名认证",notes = "实名认证成功后的信息是存在另外一张表的,非必要时,是不会通过登录等接口返回的,这里只会返回是否成功")
    @ApiImplicitParams({
            @ApiImplicitParam(name="idCard",value = "身份证号码",required = true),
            @ApiImplicitParam(name="name",value = "身份证名字",required = true),
            @ApiImplicitParam(name="uid",value = "实名认证用户的ID",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{阿里云实名认证结果的JSON字符串}"))
    @RequestMapping(value="realName",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg realName(String idCard,String name,Integer uid){
        Setting setting=settingConfiguration.getSetting();
        String host = "https://idcert.market.alicloudapi.com";
        String path = "/idcard";
        String method = "GET";
        String appcode = setting.getAli_sm_appcode();
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("idCard", idCard);
        querys.put("name", name);
        JSONObject resultObject=new JSONObject();
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
            resultObject=JSONObject.fromObject(EntityUtils.toString(response.getEntity()));
            if(resultObject.containsKey("status")){
                if(!"01".equals(resultObject.get("status"))){
                    return ResultMsg.fail(resultObject.containsKey("msg")?resultObject.get("msg").toString():"未知错误",null,resultObject);
                }else{
                    UserInfo userInfo=userInfoService.selectById(uid);
                    if(userInfo!=null){
                        userInfo.setRealName(resultObject.getString("name"));//实名认证后才有,根据这个字段是否为空判断用户进行了实名认证没
                        userInfo.setIdCard(resultObject.getString("idCard"));
                        userInfoService.updateById(userInfo);
                    }else{
                        userInfo.setUserId(uid);
                        userInfo.setRealName(resultObject.getString("name"));//实名认证后才有,根据这个字段是否为空判断用户进行了实名认证没
                        userInfo.setIdCard(resultObject.getString("idCard"));
                        userInfo.setCredits(0);
                        userInfo.setMoney(0);
                        userInfo.setLoginIp("192.168.0.0.1");
                        userInfo.setCreateTime(new DateTime());
                        userInfo.setJoinClub(0);
                        userInfo.setAppointment(0);
                        userInfo.setEnlightening(0);
                        userInfoService.insert(userInfo);
                    }
                }
            }else{
                return ResultMsg.fail("未知错误",null,resultObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMsg.success(resultObject.getString("msg"),null,resultObject);
    }

    @ApiOperation(value = "返回用户评级(星厨评级)等级评价指标体系集合",notes = "集合内对象嵌套集合,一共三层,第一层是用来分上下版显示,第二层是显示每一版的项目名称,第三层就是每个项目的选项等级分数等具体内容")
    @ApiResponses(@ApiResponse(code = 200, message = "data:[" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "    {" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "      name: 一级板块名称(没什么用)," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "      id: 该板块ID," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "      targets: [该板块包含哪些项目" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "        {" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "          name: 项目名称(用于展示)," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "          id: 项目ID," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "          targets: [该项目有哪几个选项" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "            {" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "              score: 选项对应分数," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "              grade: 选项对应等级," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "              name: 选项用于展示的名称," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "              pid: 选项的父ID," + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "              id: 选项自身ID" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "            }" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "          ]" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "        }" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "      ]" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "    }" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "  ]"+ "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "注意:用户在选择完之后,要以同样格式!要以同样格式!要以同样格式!将三级,也就是最里面\"选项\"有选中的包装起来提交,三级没有选中的,该三级所在的二级就不用包装,如果同一个一级下的二级都没有选中三级,那这个一级也不用包装给我,最后是JSON字符串形式提交"))
    @RequestMapping(value="getTargetJSONObjectList",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getTargetJSONObjectList(){
        List<Map<String,Object>>targets=dao.selectBySQL("select id,name from "+FSS.target+" where pid='0'");
        for (Map<String, Object> target : targets) {
            List<Map<String,Object>>list=dao.selectBySQL("select id,name from "+FSS.target+" where pid='"+target.get("id")+"'");
            for (Map<String, Object> map : list) {
                map.put("targets",dao.selectBySQL("select * from "+FSS.target+" where pid='"+map.get("id")+"'"));
            }
            target.put("targets",list);
        }
        return ResultMsg.success("成功",null,targets);
    }


}
