package com.stylefeng.guns.api.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.*;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.model.UserInfo;
import com.stylefeng.guns.modular.system.model.UserTarget;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.stylefeng.guns.modular.lijun.util.Tool.notEmptySQL;


/**
 * 前台用户登录接口实现类
 */
@RestController
@RequestMapping("userApi")
public class userApiInteface extends PublicMethod{


    @ApiOperation(value = "获取手机验证码",notes = "根据手机号生成验证码以短信形式发送,返回的内容里会有成功与否的消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{空,但是要注意看detail的返回内容}"))
    @RequestMapping(value="getVerificationCode",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getVerificationCode(String phone){
        if(Tool.isNull(phone))return ResultMsg.fail("请输入手机号",null,null);
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
                return ResultMsg.fail("短信发送平台出现未知错误",result.toString(),null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMsg.fail("系统错误",e.toString(),null);
        }
    }

    @ApiOperation(value = "用户登录",notes = "根据用户手机号和验证码登录并且记录用户信息,然后将更新后的用户信息返回")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号",required = true),
            @ApiImplicitParam(name="code",value = "验证码",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "\n" +
            "      birthday:生日,\n" +
            "      createtime:注册时间,\n" +
            "      sex:性别;(0:其他;1:男;2:女),\n" +
            "      avatar:头像,\n"  +
            "      apiToken:登录状态的TOKEN,\n" +
            "      phone:手机号,\n" +
            "      name:昵称,\n" +
            "      id:用户ID,\n" +
            "      email:邮箱\n" +
            "\r" +
            "      }"))
    @RequestMapping(value = "userLogin",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg userLogin(String phone,String code){
        try {
            if(Tool.isNull(phone))return ResultMsg.fail("请输入手机号",null,null);
            if(!"localhost".equals(Tool.getDomain())&&!"192.168.1.21".equals(Tool.getDomain())&&!"192.168.1.28".equals(Tool.getDomain())){
                Object session_code=((HttpSession)Tool.getRequest_Response_Session()[2]).getAttribute(FSS.LOGIN_CODE);
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
                userInfo.setLoginIp("192.168.0.1");
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

    @ApiOperation(value = "用户实名认证",notes = "该接口需要登录状态下,实名认证成功后的信息是存在另外一张表的,非必要时,是不会通过登录等接口返回的,这里只会返回是否成功")
    @ApiImplicitParams({
            @ApiImplicitParam(name="idCard",value = "身份证号码",required = true),
            @ApiImplicitParam(name="name",value = "身份证名字",required = true),
            @ApiImplicitParam(name="phone",value = "用户手机号",required = true),
            @ApiImplicitParam(name="token",value = "用户登录的最新token",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{阿里云实名认证结果的JSON字符串}"))
    @RequestMapping(value="realName",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg realName(String idCard,String name,String phone,String token){
        try {
            if(Tool.isNull(idCard))return ResultMsg.fail("请输身份证号码","idCard",null);
            if(Tool.isNull(name))return ResultMsg.fail("请输入身份证对应姓名","name",null);
            if(Tool.isNull(phone))return ResultMsg.fail("缺少参数","phone",null);
            if(Tool.isNull(token))return ResultMsg.fail("缺少参数","token",null);
            List<Map<String, Object>> users = getUser(phone, token);
            if (Tool.listIsNull(users)) return ResultMsg.fail("请登录", null, null);
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
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
            String responseEntity=EntityUtils.toString(response.getEntity());
            JSONObject resultObject=resultObject=JSONObject.fromObject(!Tool.isNull(responseEntity)&&responseEntity.startsWith("{")&&responseEntity.endsWith("}")?responseEntity:"{}");
            if(resultObject.containsKey("status")){
                if(!"01".equals(resultObject.get("status"))){
                    return ResultMsg.fail(resultObject.containsKey("msg")?resultObject.get("msg").toString():"未知错误",null,resultObject);
                }else{
                    UserInfo userInfo=userInfoService.selectById(users.get(0).get("info_id").toString());
                    if(userInfo!=null){
                        userInfo.setRealName(resultObject.getString("name"));//实名认证后才有,根据这个字段是否为空判断用户进行了实名认证没
                        userInfo.setIdCard(resultObject.getString("idCard"));
                        userInfoService.updateById(userInfo);
                    }else{
                        userInfo.setUserId(Integer.parseInt(users.get(0).get("id").toString()));
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
                return ResultMsg.success(resultObject.getString("msg"),null,resultObject);
            }else{
                return ResultMsg.fail("实名认证发生未知错误",null,responseEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultMsg.fail("系统错误",e.toString(),null);
        }
    }

    @ApiOperation(value = "获取用户评级(星厨评级)等级评价指标体系集合",notes = "该接口需要登录状态下,集合内对象嵌套集合,一共三层,第一层是用来分上下版显示,第二层是显示每一版的项目名称,第三层就是每个项目的选项等级分数等具体内容,拿到对应用户的等级评价指标ID集合(userTargetIds)后,在遍历第三层(第二层的选项)时,对比着,将对应ID设置为选中状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "用户手机号",required = true),
            @ApiImplicitParam(name="token",value = "用户登录的最新token",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "&nbsp;"+"targets:\n" +
            "&nbsp;&nbsp;"+"[\n" +
            "&nbsp;&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"name: 一级板块名称(没什么用),\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"id: 该板块ID,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"targets:\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"[\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"name: 项目名称(用于展示),\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"id: 项目ID,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"targets:\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"[\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"score: 选项对应分数,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"grade: 选项对应等级,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"name: 选项用于展示的名称,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"pid: 选项的父ID,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"id: 选项自身ID\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"]\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"]\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"],\n"+
            "&nbsp;&nbsp;&nbsp;"+"userTargetIds:\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"[\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"用户提交过审核的(没提交过审核的,你传了用户id这里就是个空数组)第三层(选项)对应的ID数组\n"+
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"]\n" +
            "&nbsp;&nbsp;&nbsp;"+"}\n"+
            "注意:用户在选择完之后,要以同样格式!要以同样格式!要以同样格式!将三级,也就是最里面选项有选中的包装起来提交,三级没有选中的,该三级所在的二级就不用包装,如果同一个一级下的二级都没有选中三级,那这个一级也不用包装给我,最后是JSON字符串形式提交"))
    @RequestMapping(value="getTargetJSONObjectList",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getTargetJSONObjectList(String phone,String token) {
        try{
            if(Tool.isNull(phone))return ResultMsg.fail("缺少参数","phone",null);
            if(Tool.isNull(token))return ResultMsg.fail("缺少参数","token",null);
            List<Map<String, Object>> users = getUser(phone, token);
            if (Tool.listIsNull(users)) return ResultMsg.fail("请登录", null, null);
            List<Map<String,Object>>targets=dao.selectBySQL("select id,name from "+FSS.target+" where pid='0'");
            Map<String,Object>result=new HashMap<>();
            for (Map<String, Object> target : targets) {
                List<Map<String,Object>>list=dao.selectBySQL("select id,name from "+FSS.target+" where pid='"+target.get("id")+"'");
                for (Map<String, Object> map : list) {
                    map.put("targets",dao.selectBySQL("select * from "+FSS.target+" where pid='"+map.get("id")+"'"));
                }
                target.put("targets",list);
            }
            result.put("targets",targets);
            if(!Tool.listIsNull(users)){
                Set<String> targetSelectIds=new HashSet<>();
                UserTarget userTarget = userTargetService.selectOne(new EntityWrapper<>(new UserTarget()).eq("uid", users.get(0).get("id")));
                if(userTarget!=null){
                    List<Map<String,Object>> thisTargetArray=!Tool.isNull(userTarget.getTarget())&&userTarget.getTarget().startsWith("[")&&userTarget.getTarget().endsWith("]")?JSONArray.fromObject(userTarget.getTarget()):JSONArray.fromObject("[]");
                    for (Map<String, Object> stringObjectMap : thisTargetArray) {
                        if(Tool.mapGetKeyNotEmpty(stringObjectMap,"targets")&&stringObjectMap.get("targets").toString().startsWith("[")&&stringObjectMap.get("targets").toString().endsWith("]")){
                            List<Map<String,Object>> thisTargetArray1=JSONArray.fromObject(stringObjectMap.get("targets"));
                            for (Map<String, Object> objectMap : thisTargetArray1) {
                                if(Tool.mapGetKeyNotEmpty(objectMap,"targets")&&objectMap.get("targets").toString().startsWith("[")&&objectMap.get("targets").toString().endsWith("]")){
                                    List<Map<String,Object>> thisTargetArray2=JSONArray.fromObject(objectMap.get("targets"));
                                    for (Map<String, Object> map : thisTargetArray2) {
                                        if(!Tool.isNull(map.get("id")))targetSelectIds.add(map.get("id").toString());
                                    }
                                }
                            }
                        }
                    }
                }
                result.put("userTargetIds",targetSelectIds);
            }
            return ResultMsg.success("成功",null,result);
        }catch (Exception e) {
            e.printStackTrace();
            return  ResultMsg.fail("系统错误",e.toString(),null);
        }
    }
    @ApiOperation(value = "提交用户等级审核信息",notes = "该接口会根据targetJSONObject字段进行会员等级评分,最终评级成功后,如果系统评级达到4级或者4级以上,是需要用户进行线下交材料进行认证的")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "用户手机号",required = true),
            @ApiImplicitParam(name="token",value = "用户登录的最新token",required = true),
            @ApiImplicitParam(name="targetJSONObject",value = "用户等级选项(格式参考/userApi/getTargetJSONObjectList接口返回的data对象里的targets对象格式,三级没有选中的,该三级所在的二级就不用包装,如果同一个一级下的二级都没有选中三级,那这个一级也不用包装,最后是JSON字符串形式提交)",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "注意返回值里的detail字段的内容提示,该接口data字段没有返回值"))
    @RequestMapping(value = "submitUserTargetById",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg submitUserTargetById(String phone,String token,String targetJSONObject){
        try{
            if(Tool.isNull(phone))return ResultMsg.fail("缺少参数","phone",null);
            if(Tool.isNull(token))return ResultMsg.fail("缺少参数","token",null);
            List<Map<String, Object>> users = getUser(phone, token);
            if (Tool.listIsNull(users)) return ResultMsg.fail("请登录", null, null);
//            List<Map<String,Object>>targets=!Tool.isNull(targetJSONObject)&&targetJSONObject.startsWith("[")&&targetJSONObject.endsWith("]")?JSONArray.fromObject(targetJSONObject):JSONArray.fromObject("[]");
            int score=0;
            List<Map<String,Object>>targets=JSONArray.fromObject(targetJSONObject);
            for (Map<String, Object> target : targets) {
                if(!Tool.mapGetKeyNotEmpty(target,"name"))return ResultMsg.fail("参数异常","targetJSONObject一级分类缺少参数'name'",null);
                if(!Tool.mapGetKeyNotEmpty(target,"id"))return ResultMsg.fail("参数异常","targetJSONObject一级分类缺少参数'id'",null);
                List<Map<String,Object>>targets2=JSONArray.fromObject(target.get("targets"));
                for (Map<String, Object> target2 : targets2) {
                    if(!Tool.mapGetKeyNotEmpty(target2,"name"))return ResultMsg.fail("参数异常","targetJSONObject二级分类缺少参数'name'",null);
                    if(!Tool.mapGetKeyNotEmpty(target2,"id"))return ResultMsg.fail("参数异常","targetJSONObject二级分类缺少参数'id'",null);
                    List<Map<String,Object>>targets3=JSONArray.fromObject(target2.get("targets"));
                    if(targets3.size()>1)return ResultMsg.fail("参数异常","二级分类:"+target2.get("name")+",的三级分类数量超过1个'",null);
                    for (Map<String, Object> target3 : targets3) {
                        if(!Tool.mapGetKeyNotEmpty(target3,"score"))return ResultMsg.fail("参数异常","targetJSONObject三级分类缺少参数'score'",null);
                        if(!Tool.mapGetKeyNotEmpty(target3,"grade"))return ResultMsg.fail("参数异常","targetJSONObject三级分类缺少参数'grade'",null);
                        if(!Tool.mapGetKeyNotEmpty(target3,"name"))return ResultMsg.fail("参数异常","targetJSONObject三级分类缺少参数'name'",null);
                        if(!Tool.mapGetKeyNotEmpty(target3,"pid"))return ResultMsg.fail("参数异常","targetJSONObject三级分类缺少参数'pid'",null);
                        if(!Tool.mapGetKeyNotEmpty(target3,"id"))return ResultMsg.fail("参数异常","targetJSONObject三级分类缺少参数'id'",null);
                        score+=Integer.valueOf(target3.get("score").toString());
                    }
                }
            }
            String lv=score>95?"6":score>63?"5":score>31?"4":score>23?"3":score>7?"2":"1";
            String check=score>31?"0":"1";
            String time= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            List<Map<String ,Object>>myUserTargets=dao.selectBySQL("select * from "+FSS.user_target+" where uid='"+users.get(0).get("id")+"'");
            if(Tool.listIsNull(myUserTargets)){
                dao.insertBySQL("insert into "+FSS.user_target+" (uid,target,lv,check,replenish,createtime,updatetime) values" +
                        " ('"+users.get(0).get("id")+"','"+targetJSONObject+"','"+lv+"','"+check+"','[]','"+time+"','"+time+"')");
            }else{
                dao.updateBySQL("update "+FSS.user_target+" set target='"+targetJSONObject+"',lv='"+lv+"',check='"+check+"',updatetime='"+time+"' where uid='"+users.get(0).get("id")+"'");
            }
            return ResultMsg.success(("1".equals(check)?"提交成功":"提交成功,您的会员等级已经达到4级或4级以上,需要您到线下进行认证"),null,null);
        }catch (JSONException JSONe){
            JSONe.printStackTrace();
            return  ResultMsg.fail("系统错误",JSONe.toString(),"中文意思就是,你传的targetJSONObject字段的值不是后台要的JSON格式字符串,后台要的是JSON数组格式的,请参照/userApi/getTargetJSONObjectList接口返回的targets");
        }catch (Exception e){
            e.printStackTrace();
            return  ResultMsg.fail("系统错误",e.toString(),null);
        }
    }

    @ApiOperation(value = "首页→加精界面接口",notes = "该接口返回data分为三个大板块:顶部推荐厨师分组→chefs,中部跳转模块→jumpPages,加精内容→news,加精内容(news)是个数组,长度最多为16,数组内对象已经按照UI图去掉不需要内容以及排列好顺序,只需要拿来循环,根据数组内每个对象的type字段(必定会有),来排列每个对象的data(必定会有)字段.另:数组内每个对象可根据type字段来建立,每个对象即使遇到空值或者空数组,我也会填充成空字符串或者空数组")
    @ApiImplicitParams({
            @ApiImplicitParam(name="lng_x",value = "定位坐标,lng或x轴",required = true),
            @ApiImplicitParam(name="lat_y",value = "定位坐标,lat或y轴",required = true)
    })
    @ApiResponses(@ApiResponse(code = 200, message = "data:{\n" +
            "&nbsp;"+"chefs:[\n" +
            "&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;"+"name:厨师昵称(不是实名),\n" +
            "&nbsp;&nbsp;&nbsp;"+"id:用来跳转个人主页时传参,\n" +
            "&nbsp;&nbsp;&nbsp;"+"avatar:头像\n" +
            "&nbsp;&nbsp;"+"},\n" +
            "&nbsp;&nbsp;"+"...\n" +
            "&nbsp;"+"],\n" +
            "&nbsp;"+"jumpPages:[\n" +
            "&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;"+"code:跳转页面代号(最后再做介绍),\n" +
            "&nbsp;&nbsp;&nbsp;"+"picture:背景图\n" +
            "&nbsp;&nbsp;"+"},\n" +
            "&nbsp;&nbsp;"+"...\n" +
            "&nbsp;"+"],\n" +
            "&nbsp;"+"news:[\n" +
            "&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;"+"type:"+ FSS.activity + "\n" +
            "&nbsp;&nbsp;&nbsp;"+"data:{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"thumb:图片,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"hot:true有火焰图标false没有火焰图标,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"id:跳转活动详情页传参,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"applyUsers:[\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"id:用户ID(可跳转个人主页传参用),\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"avatar:头像图片\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"},\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"...\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"]\n" +
            "&nbsp;&nbsp;"+"},\n" +
            "&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;"+"type:"+ FSS.banner + "\n" +
            "&nbsp;&nbsp;&nbsp;"+"data:{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"href:广告跳转H5的超链接,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"title:标题,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"hot:true有火焰图标false没有火焰图标,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"picture:图片,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"tagNames:[标签名称数组,...]\n" +
            "&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;"+"},\n" +
            "&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;"+"type:"+ FSS.information + "\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"data:{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"thumb:图片,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"description:简介文字,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"title:标题,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"hot:true有火焰图标false没有火焰图标,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"column_id:栏目名,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"id:跳转资讯详情页传参用\n" +
            "&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;"+"},\n" +
            "&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;"+"type:"+ FSS.classroom + "\n" +
            "&nbsp;&nbsp;&nbsp;"+"data:{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"thumb:图片,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"title:标题,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"hot:true有火焰图标false没有火焰图标,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"column_id:栏目名,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"id:跳转星厨课堂详情页传参用\n" +
            "&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;"+"},\n" +
            "&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;"+"type:"+ FSS.works + "_user\n" +
            "&nbsp;&nbsp;&nbsp;"+"data:{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"thumb:图片,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"hot:true有火焰图标false没有火焰图标,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"name:标题,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"id:跳转作品详情页传参用,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"user:{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"avatar:头像图片\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"id:用户ID(可跳转个人主页传参用),\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"name:用户昵称\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;"+"},\n" +
            "&nbsp;&nbsp;"+"{\n" +
            "&nbsp;&nbsp;&nbsp;"+"type:"+ FSS.works + "_manager\n" +
            "&nbsp;&nbsp;&nbsp;"+"data:{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"thumb:图片,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"hot:true有火焰图标false没有火焰图标,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"name:标题,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"id:跳转作品详情页传参用,\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"user:{\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"avatar:头像图片\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"id:餐厅ID(可跳转餐厅主页传参用),\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"name:餐厅名称\n" +
            "&nbsp;&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;&nbsp;"+"}\n" +
            "&nbsp;&nbsp;"+"},\n" +
            "&nbsp;&nbsp;"+"...\n" +
            "&nbsp;"+"]\n" +
            ""+"}\n" +
            "最后对于jumpPages做一个约定说明:0→星厨俱乐部,1→拜师学艺,2→厨友圈,3→赛事活动,4→宴席预定,5→星厨招聘"))
    @RequestMapping(value = "indexPartStatic",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg indexPartStatic(String lng_x,String lat_y){
        if(Tool.isNull(lng_x))return ResultMsg.fail("缺少参数","lng_x",null);
        if(Tool.isNull(lat_y))return ResultMsg.fail("缺少参数","lat_y",null);
        try{
            //把加精的广告拿最多两条出来,备用(t1.id后加-ROUND(RAND())是为了让有时候一条广告都出不来的效果)
            List<Map<String,Object>>banners=dao.selectBySQL("SELECT * FROM "+FSS.banner+" AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX(id) FROM "+FSS.banner+")-(SELECT MIN(id) FROM "+FSS.banner+"))+(SELECT MIN(id) FROM "+FSS.banner+")) AS id) AS t2 WHERE t1.id-ROUND(RAND()) >= t2.id and is_ok='1' and type='"+FSS.banner+"' and FIND_IN_SET('-1',tag_id) order by submit_time desc LIMIT "+new Random().nextInt(2)+",2");
            for (int i = 0; i < banners.size(); i++) {
                if(!Tool.isNull(banners.get(i).get("tag_id"))&&Arrays.asList(banners.get(i).get("tag_id").toString().trim().split(",")).contains("-2")){
                    banners.get(i).put("hot",true);
                }else{
                    banners.get(i).put("hot",false);
                }
                List<Map<String,Object>>tags=dao.selectBySQL("select * from "+FSS.tag+" where id in("+banners.get(i).get("tag_id")+") and id not in(-1,-2)");
                List<String>tagNames=new ArrayList<>();
                for (Map<String, Object> tag : tags) {
                    if(!Tool.isNull(tag.get("name")))tagNames.add(tag.get("name").toString());
                }
                banners.get(i).put("tagNames",tagNames);
                Tool.removeMapParmeByKey(banners.get(i),new String[]{"item_id","submit_time","object_name","id","type","is_ok","tag_id"});
            }
            Map<String,Object>result=new HashMap<>();
            //开始获取推荐厨师
            //1.拉取厨师分组的所有城市ID,用这些城市ID找出城市集合
            List<String>cityIds=new ArrayList<>();
            List<Map<String,Object>>groupUsers=dao.selectBySQL("select * from "+FSS.index_up_group_user+" where city_id<>0 and "+ notEmptySQL("city_id"));
            for (Map<String, Object> groupUser : groupUsers) {
                cityIds.add(groupUser.get("city_id").toString());
            }
            List<Map<String,Object>>citys=dao.selectBySQL("select * from "+FSS.city+" where id in("+ StringUtils.join(cityIds,",")+") and"+notEmptySQL("lngx","laty"));
            //注意上面的citys并不代表所有城市,而是被推荐厨师表限制的城市,所以要使用城市最好使用下面的citysAll
            List<Map<String,Object>>citysAll=dao.selectBySQL("select * from "+FSS.city+" where "+notEmptySQL("lngx","laty"));
            //2.在城市集合中,用前台传进来的lng_x和lat_y两个坐标找出距离最近的城市,的ID
            Map<String,Object>userGroupCity=null;
            double tempDistanceOne=Double.MAX_VALUE;
            for (int i = 0; i < citys.size(); i++) {
                double tempDistanceTwo=LocationUtils.getDistance(Double.valueOf(lat_y),Double.valueOf(lng_x),Double.valueOf(citys.get(i).get("laty").toString()),Double.valueOf(citys.get(i).get("lngx").toString()));
                citys.get(i).put("distance",tempDistanceTwo);//把每个城市距离登录用户的距离记录一下
                if(tempDistanceTwo<tempDistanceOne){
                    tempDistanceOne=tempDistanceTwo;
                    userGroupCity=citys.get(i);
                }
            }
            //这个cityAll是距离用户最近的city,而不是上面的根据推荐厨师所有的city_id决定的
            Map<String,Object>cityAll=null;
            double tempDistanceOne_=Double.MAX_VALUE;
            for (int i = 0; i < citysAll.size(); i++) {
                double tempDistanceTwo=LocationUtils.getDistance(Double.valueOf(lat_y),Double.valueOf(lng_x),Double.valueOf(citysAll.get(i).get("laty").toString()),Double.valueOf(citysAll.get(i).get("lngx").toString()));
                citysAll.get(i).put("distance",tempDistanceTwo);//把每个城市距离登录用户的距离记录一下
                if(tempDistanceTwo<tempDistanceOne_){
                    tempDistanceOne_=tempDistanceTwo;
                    cityAll=citysAll.get(i);
                }
            }
            citysAll=Tool.ListMapOrderByMapKeyDesc(citysAll,new String[]{"distance"});//根据距离由远到近排列一下
            List<String>orderDistanceAscCityIds=new ArrayList<>();
            for (int i = citysAll.size() - 1; i >= 0; i--) {
                //再由近到远拿出来单独放着备用(目前就餐厅作品用到,后期修改逻辑可能其他地方也要用)
                if(!Tool.isNull(citysAll.get(i).get("id")))orderDistanceAscCityIds.add("'"+citysAll.get(i).get("id").toString().trim()+"'");
            }
            //3.用最近的城市的id找到对应分组的厨师们,再加上城市ID等于0也就是分组为"全国"的厨师们,放到要返回的结果result里去(这里要特别注意顺序一定要符合数据库里用户的ID顺序)
            LinkedHashSet<String>userIDs=new LinkedHashSet<>();
            if(userGroupCity!=null){
                List<Map<String,Object>>groups=dao.selectBySQL("select * from "+FSS.index_up_group_user+" where city_id='"+userGroupCity.get("id")+"'");
                for (Map<String, Object> group : groups) {
                    if(Tool.mapGetKeyNotEmpty(group,"user_api_id")){
                        for (String user_api_id : group.get("user_api_id").toString().split(",")) {
                            userIDs.add(user_api_id);
                        }
                    }
                }
            }
            List<Map<String,Object>>groupNationwides=dao.selectBySQL("select * from "+FSS.index_up_group_user+" where city_id='0'");
            for (Map<String, Object> groupNationwide : groupNationwides) {
                if(Tool.mapGetKeyNotEmpty(groupNationwide,"user_api_id")){
                    for (String user_api_id : groupNationwide.get("user_api_id").toString().split(",")) {
                        userIDs.add(user_api_id);
                    }
                }
            }
            List<Map<String,Object>>userApis=dao.selectBySQL("select * from "+FSS.user_api+" where id in("+StringUtils.join(userIDs,",")+") order by FIELD(id,"+StringUtils.join(userIDs,",")+")");
            for (int i = 0; i < userApis.size(); i++) {
                //待处理数据,等接口写完后再对照UI处理
                Tool.removeMapParmeByKey(userApis.get(i),new String[]{"createtime","phone","sex","sex","object_name"});
            }
            result.put("chefs",userApis);

            //开始放入跳转板块
            List<Map<String,Object>>jumpPages=dao.selectBySQL("select * from "+FSS.jump_page+" where enable='1' order by orders asc");
            for (int i = 0; i < jumpPages.size(); i++) {
                //待处理数据,等接口写完后再对照UI处理
                Tool.removeMapParmeByKey(jumpPages.get(i),new String[]{"enable","object_name","id","orders"});
            }
            result.put("jumpPages",jumpPages);

            //开始放入加精内容(资讯/课堂按照 首页加精→最新修改时间排序 作品(用户-餐厅)按照 定位最近→最新修改时间排序) 活动 满足加精，定位最近→最新开始时间(startTime)
            //1活动,3资讯,1课堂,3资讯,1用户作品,3资讯,1餐厅作品,3资讯
            //每个map有一个type字段,用以区分展示的是大图小图
            //①　加精内容推荐1号位和2号位之间②　加精内容推荐5号位和6号位之间
            List<Map<String,Object>>news=new ArrayList<>();
            //先拿12条资讯,并处理好数据,不然进入下面for循环不好弄,进去后把12条资讯按照位置塞进去;
            List<Map<String,Object>>informations=dao.selectBySQL("select * from "+FSS.information+" where FIND_IN_SET('-1',tag_id) order by create_time desc,update_time desc limit 0,12");
            for (int i = 0; i < informations.size(); i++) {
                //待处理数据,等接口写完后再对照UI处理
                if(!Tool.isNull(informations.get(i).get("column_id"))){
                    informations.get(i).put("column_id",getColumnNameByColumnId(informations.get(i).get("column_id")));
                }
                if(!Tool.isNull(informations.get(i).get("tag_id"))&&informations.get(i).get("tag_id").toString().contains("-2")){
                    informations.get(i).put("hot",true);
                }else{
                    informations.get(i).put("hot",false);
                }
                Tool.removeMapParmeByKey(informations.get(i),new String[]{"create_time","content","uid","update_time","publish_ip","source_id","city_id","images","tag_id"});
            }
            //拿到定位最近的城市,上面已经拿到了,变量名为:city,city为Map<String,Object>,且使用时要防止city为null,为null时,筛选条件就没有city_id,为null时,还说明了,数据库里sys_city所有数据都没有坐标;
            for (int i = 0; i < 16; i++) {
                if(i==0){//1活动---加精,定位最近→最新开始时间(startTime)排序   之后有广告
                    List<Map<String,Object>>activitys=dao.selectBySQL("select * from (" +
                            "select * from (SELECT * FROM "+FSS.activity+" where is_ok='1' and FIND_IN_SET('-1',tag_id) and start_time>NOW() "+(cityAll!=null?("and city_id='"+cityAll.get("id")+"'"):"")+" ORDER BY start_time desc limit 10)a" +
                            " union " +
                            "select * from (SELECT * FROM "+FSS.activity+" where is_ok='1' and FIND_IN_SET('-1',tag_id) and start_time>NOW() "+(cityAll!=null?("and city_id<>'"+cityAll.get("id")+"'"):"")+" ORDER BY start_time desc limit 10)b" +
                            " union " +
                            "select * from (SELECT * FROM "+FSS.activity+" where is_ok='1' and FIND_IN_SET('-1',tag_id) and start_time<NOW() "+(cityAll!=null?("and city_id='"+cityAll.get("id")+"'"):"")+" ORDER BY start_time desc limit 10)c" +
                            " union " +
                            "select * from (SELECT * FROM "+FSS.activity+" where is_ok='1' and FIND_IN_SET('-1',tag_id) and start_time<NOW() "+(cityAll!=null?("and city_id<>'"+cityAll.get("id")+"'"):"")+" ORDER BY start_time desc limit 10)d" +
                            ")e limit 0,1");
                    if(!Tool.listIsNull(activitys)){
                        Map<String,Object>activity=new HashMap<>();
                        activity.put("type",FSS.activity);
                        //往活动里面塞报名用户[Map集合],最多5个用户
                        List<Map<String,Object>>activityApplys=dao.selectBySQL("select * from "+FSS.activity_apply+" where activity_id='"+activitys.get(0).get("id")+"'");
                        List<Map<String,Object>>activityUserApis=new ArrayList<>();
                        if(!Tool.listIsNull(activityApplys)&&!Tool.isNull(activityApplys.get(0).get("user_api_id"))){
                            List<String>user_api_ids=Arrays.asList(activityApplys.get(0).get("user_api_id").toString().split(","));
                            Collections.reverse(user_api_ids);//select出来的用户顺序是正序的,放页面上需要倒过来最新的5个或少于5个的用户
                            activityUserApis=dao.selectBySQL("select * from "+FSS.user_api+" where id in("+StringUtils.join(user_api_ids,",")+") order by FIELD(id,"+StringUtils.join(user_api_ids,",")+") limit 0,5");
                            for (int i1 = 0; i1 < activityUserApis.size(); i1++) {
                                Tool.removeMapParmeByKey(activityUserApis.get(i1),new String[]{"createtime","phone","sex","object_name","name"});
                            }
                        }
                        activitys.get(0).put("applyUsers",activityUserApis);
                        activitys.get(0).put("thumb",Tool.mapGetKeyNotEmpty(activitys.get(0),"thumb")&&activitys.get(0).get("thumb").toString().startsWith("[")&&activitys.get(0).get("thumb").toString().endsWith("]")&&!JSONArray.fromObject(activitys.get(0).get("thumb").toString()).isEmpty()?JSONObject.fromObject(JSONArray.fromObject(activitys.get(0).get("thumb").toString()).get(0)).get("url"):"");
                        if(!Tool.isNull(activitys.get(0).get("tag_id"))&&Arrays.asList(activitys.get(0).get("tag_id").toString().trim().split(",")).contains("-2")){
                            activitys.get(0).put("hot",true);
                        }else{
                            activitys.get(0).put("hot",false);
                        }
                        Tool.removeMapParmeByKey(activitys.get(0),new String[]{"create_time","content","update_time","publish_ip","tag_id","source_id","description"
                                ,"is_ok","apply_num","city_id","uid","video","video_object_name","end_time","start_time"});
                        activity.put("data",activitys.get(0));
                        news.add(activity);
                    }
                    //加广告
                    if(!Tool.listIsNull(banners)){
                        Map<String,Object>banner=new HashMap<>();
                        banner.put("type",FSS.banner);
                        banner.put("data",banners.get(0));
                        news.add(banner);
                    }
                }if(i>0&&i<4){//3资讯---首页加精→最新修改时间排序
                    newsPutInformation(news, informations);
                }if(i==4){//1课堂---首页加精→最新修改时间排序   之后有广告
                    List<Map<String,Object>>classrooms=dao.selectBySQL("select * from "+FSS.classroom+" where FIND_IN_SET('-1',tag_id) order by update_time desc limit 0,1");
                    if(!Tool.listIsNull(classrooms)){
                        Map<String,Object>classroom=new HashMap<>();
                        classroom.put("type",FSS.classroom);
                        if(!Tool.isNull(classrooms.get(0).get("column_id"))){
                            classrooms.get(0).put("column_id",getColumnNameByColumnId(classrooms.get(0).get("column_id")));
                        }
                        if(!Tool.isNull(classrooms.get(0).get("tag_id"))&&Arrays.asList(classrooms.get(0).get("tag_id").toString().trim().split(",")).contains("-2")){
                            classrooms.get(0).put("hot",true);
                        }else{
                            classrooms.get(0).put("hot",false);
                        }
                        Tool.removeMapParmeByKey(classrooms.get(0),new String[]{"images","create_time","description","video","sort","coverphoto","content","start_time","update_time","shor_title","shor_title","user_description","city_id","posters_title","tag_id"});
                        classroom.put("data",classrooms.get(0));
                        news.add(classroom);
                    }
                    //加广告
                    if(!Tool.listIsNull(banners)&&banners.size()>1){
                        Map<String,Object>banner=new HashMap<>();
                        banner.put("type",FSS.banner);
                        banner.put("data",banners.get(1));
                        news.add(banner);
                    }
                }if(i>4&&i<8){//3资讯---首页加精→最新修改时间排序
                    newsPutInformation(news, informations);
                }if(i==8){//1用户作品---定位最近→最新修改时间排序
                    //最近登录,且优先是city这个城市的100人
                    List<Map<String,Object>>userInfos=dao.selectBySQL("SELECT * FROM "+FSS.user_info+" AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX(id) FROM "+FSS.user_info+")-(SELECT MIN(id) FROM "+FSS.user_info+"))+(SELECT MIN(id) FROM "+FSS.user_info+")) AS id) AS t2 WHERE t1.id >= t2.id ORDER BY "+(cityAll!=null?("FIELD(city_id,'"+cityAll.get("id")+"') desc,"):"")+"t1.update_time desc LIMIT 100");
                    if(!Tool.listIsNull(userInfos)){
                        List<String>userIds=new ArrayList<>();
                        for (int i1 = 0; i1 < userInfos.size(); i1++) {
                            if(!Tool.isNull(userInfos.get(i1).get("user_id"))){
                                userIds.add("'"+userInfos.get(i1).get("user_id")+"'");
                            }
                        }
                        List<Map<String,Object>>workss=new ArrayList<>();
                        //拿这100人去找他们加精的,最新的作品,找出来,取第一个
                        workss=dao.selectBySQL("select * from "+FSS.works+" where status='1' and role='userapi' and FIND_IN_SET('-1',tag_id) and user_id in("+StringUtils.join(userIds,",")+") order by update_time desc limit 1");
                        if(!Tool.listIsNull(workss)){//如果有,就塞到news里
                            Map<String,Object>works=new HashMap<>();
                            works.put("type",FSS.works+"_user");
                            repeatWoksFragment(workss,0);
                            works.put("data",workss.get(0));
                            news.add(works);
                        }else{//如果没有,那就找这100人之外其他人加精的,最新的作品,找出来,取第一个
                            workss=dao.selectBySQL("select * from "+FSS.works+" where status='1' and role='userapi' and FIND_IN_SET('-1',tag_id) and user_id not in("+StringUtils.join(userIds,",")+") order by update_time desc limit 1");
                            if(!Tool.listIsNull(workss)){//如果有,就塞到news里
                                Map<String,Object>works=new HashMap<>();
                                works.put("type",FSS.works+"_user");
                                repeatWoksFragment(workss,0);
                                works.put("data",workss.get(0));
                                news.add(works);
                            }
                        }
                    }
                }if(i>8&&i<12){//3资讯---首页加精→最新修改时间排序
                    newsPutInformation(news, informations);
                }if(i==12){//1餐厅作品---定位最近→最新修改时间排序
                    //优先按照城市由近到远,取100家餐厅
                    List<Map<String,Object>>managers=dao.selectBySQL("SELECT * FROM "+FSS.restaurant_info_manager+" AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX(id) FROM "+FSS.restaurant_info_manager+")-(SELECT MIN(id) FROM "+FSS.restaurant_info_manager+"))+(SELECT MIN(id) FROM "+FSS.restaurant_info_manager+")) AS id) AS t2 WHERE t1.id >= t2.id ORDER BY "+(!Tool.listIsNull(orderDistanceAscCityIds)?("FIELD(city_id,"+StringUtils.join(orderDistanceAscCityIds,",")+") desc"):"")+" LIMIT 100");
                    if(!Tool.listIsNull(managers)){
                        List<String>managerIds=new ArrayList<>();
                        for (int i1 = 0; i1 < managers.size(); i1++) {
                            if(!Tool.isNull(managers.get(i1).get("id"))){
                                managerIds.add("'"+managers.get(i1).get("id")+"'");
                            }
                        }
                        List<Map<String,Object>>workss=new ArrayList<>();
                        //拿这100家餐厅去找他们加精的,最新的作品,找出来,取第一个
                        workss=dao.selectBySQL("select * from "+FSS.works+" where status='1' and role='manager' and FIND_IN_SET('-1',tag_id) and user_id in("+StringUtils.join(managerIds,",")+") order by update_time desc limit 1");
                        if(!Tool.listIsNull(workss)){//如果有,就塞到news里
                            Map<String,Object>works=new HashMap<>();
                            works.put("type",FSS.works+"_manager");
                            repeatWoksFragment(workss,0);
                            works.put("data",workss.get(0));
                            news.add(works);
                        }else{//如果没有,那就找这100家餐厅之外其他餐厅加精的,最新的作品,找出来,取第一个
                            workss=dao.selectBySQL("select * from "+FSS.works+" where status='1' and role='manager' and FIND_IN_SET('-1',tag_id) and user_id not in("+StringUtils.join(managerIds,",")+") order by update_time desc limit 1");
                            if(!Tool.listIsNull(workss)){//如果有,就塞到news里
                                Map<String,Object>works=new HashMap<>();
                                works.put("type",FSS.works+"_manager");
                                repeatWoksFragment(workss,0);
                                works.put("data",workss.get(0));
                                news.add(works);
                            }
                        }
                    }
                }if(i>12){//3资讯---首页加精→最新修改时间排序
                    newsPutInformation(news, informations);
                }
            }
            result.put("news",news);
            return ResultMsg.success("查询成功",null,result);
        }catch (Exception e){
            e.printStackTrace();
            return  ResultMsg.fail("系统错误",e.toString(),null);
        }
    }

    private void repeatWoksFragment(List<Map<String, Object>> workss,int index) {
        if(!Tool.isNull(workss.get(index).get("images"))){
            List<String>thumbs=getALiOSSPircturesByBaseId(workss.get(index).get("images").toString());
            workss.get(index).put("thumb",!Tool.listIsNull(thumbs)?thumbs.get(index):"");
        }else{
            workss.get(index).put("thumb","");
        }
        if(!Tool.isNull(workss.get(index).get("tag_id"))&&Arrays.asList(workss.get(index).get("tag_id").toString().trim().split(",")).contains("-2")){
            workss.get(index).put("hot",true);
        }else{
            workss.get(index).put("hot",false);
        }
        if(!Tool.isNull(workss.get(index).get("role"))&&!Tool.isNull(workss.get(index).get("user_id"))&&("manager".equals(workss.get(index).get("role"))||"userapi".equals(workss.get(index).get("role")))){
            List<Map<String,Object>>users=dao.selectBySQL("select id"+("userapi".equals(workss.get(index).get("role"))?",name":",restaurant")+("userapi".equals(workss.get(index).get("role"))?",avatar":",thumb")+("manager".equals(workss.get(index).get("role"))?",city_id":"")+" from "+("userapi".equals(workss.get(index).get("role"))?FSS.user_api:FSS.restaurant_info_manager)+" where id="+workss.get(index).get("user_id"));
            String cityName="";
            if("manager".equals(workss.get(index).get("role"))){
//                String cityName=getCityNameByCityId();
                cityName=!Tool.listIsNull(users)&&!Tool.isNull(users.get(0).get("city_id"))?getCityNameByCityId(users.get(0).get("city_id")):"";
                if(!Tool.listIsNull(users)){
                    users.get(0).put("restaurant",users.get(0).get("restaurant")+(!Tool.isNull(cityName)?("|"+cityName):""));
                    Tool.removeMapParmeByKey(users.get(0),new String[]{"city_id"});
                }
            }
            workss.get(index).put("user",!Tool.listIsNull(users)?users.get(0):new HashMap<String,Object>());
        }else{
            workss.get(index).put("user",new HashMap<String,Object>());
        }
        Tool.removeMapParmeByKey(workss.get(index),new String[]{"seasoning","images","practice","role","create_time","supplementary_material","remark","video","main_ingredient","update_time"
        ,"status","tag_id","column_id","user_id"});
    }

    private void newsPutInformation(List<Map<String, Object>> news, List<Map<String, Object>> informations) {
        if(!Tool.listIsNull(informations)){
            Map<String,Object>information=new HashMap<>();
            information.put("type", FSS.information);
            Iterator<Map<String,Object>> it=informations.iterator();
            while (it.hasNext()){
                information.put("data",it.next());
                it.remove();
                break;
            }
            news.add(information);
        }
    }

    @ApiOperation(value = "首页→最新资讯界面接口",notes = "该接口返回data分为两个字段:hasNext→是否还有可加载内容(true/false前端可根据这个字段判断是否下次继续上划加载),最新内容→news,加精内容(news)是个数组,长度最多为11,数组内对象随机顺序,只需要拿来循环,根据数组内每个对象的type字段(必定会有),来排列每个对象的data(必定会有)字段.另:数组内每个对象可根据type字段来建立,每个对象即使遇到空值或者空数组,我也会填充成空字符串或者空数组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value="页面号,第几页的意思,从0开始。至于每页多少条,我已经固定死了,每页10条内容,另,如果满足10条内容,就随机添加或者不添加一条广告",required = true)
    })
    @ApiResponses(@ApiResponse(code=200,message="data:{\n" +
            "&nbsp;" + "news:[请参照/userApi/indexPartStatic也就是首页→加精界面接口返回值里data里news字段格式],\n" +
            "&nbsp;" + "hasNext:true或者false,是否还有下一页内容\n" +
            "}"))
    @RequestMapping(value="indexPartDynamic",method =  RequestMethod.POST)
    @ResponseBody
    public ResultMsg indexPartDynamic(String pageNum){
        try{
            if(Tool.isNull(pageNum))return ResultMsg.fail("缺少参数","pageNum",null);
            List<Map<String,Object>>news=new ArrayList<>();
            //1条活动
            List<Map<String,Object>>activitys=dao.selectBySQL("select * from "+FSS.activity+" where is_ok='1' ORDER BY update_time desc,create_time desc limit "+(Integer.valueOf(pageNum)*1)+",1");
            for (int i = 0; i < activitys.size(); i++) {
                Map<String,Object>activity=new HashMap<>();
                activity.put("type",FSS.activity);
                //往活动里面塞报名用户[Map集合],最多5个用户
                List<Map<String,Object>>activityApplys=dao.selectBySQL("select * from "+FSS.activity_apply+" where activity_id='"+activitys.get(i).get("id")+"'");
                List<Map<String,Object>>activityUserApis=new ArrayList<>();
                if(!Tool.listIsNull(activityApplys)&&!Tool.isNull(activityApplys.get(0).get("user_api_id"))){
                    List<String>user_api_ids=Arrays.asList(activityApplys.get(0).get("user_api_id").toString().split(","));
                    Collections.reverse(user_api_ids);//select出来的用户顺序是正序的,放页面上需要倒过来最新的5个或少于5个的用户
                    activityUserApis=dao.selectBySQL("select * from "+FSS.user_api+" where id in("+StringUtils.join(user_api_ids,",")+") order by FIELD(id,"+StringUtils.join(user_api_ids,",")+") limit 0,5");
                    for (int i1 = 0; i1 < activityUserApis.size(); i1++) {
                        Tool.removeMapParmeByKey(activityUserApis.get(i1),new String[]{"createtime","phone","sex","object_name","name"});
                    }
                }
                activitys.get(i).put("applyUsers",activityUserApis);
                activitys.get(i).put("thumb",Tool.mapGetKeyNotEmpty(activitys.get(i),"thumb")&&activitys.get(i).get("thumb").toString().startsWith("[")&&activitys.get(i).get("thumb").toString().endsWith("]")&&!JSONArray.fromObject(activitys.get(i).get("thumb").toString()).isEmpty()?JSONObject.fromObject(JSONArray.fromObject(activitys.get(i).get("thumb").toString()).get(i)).get("url"):"");
                if(!Tool.isNull(activitys.get(i).get("tag_id"))&&Arrays.asList(activitys.get(i).get("tag_id").toString().trim().split(",")).contains("-2")){
                    activitys.get(i).put("hot",true);
                }else{
                    activitys.get(i).put("hot",false);
                }
                Tool.removeMapParmeByKey(activitys.get(i),new String[]{"create_time","content","update_time","publish_ip","tag_id","source_id","description"
                        ,"is_ok","apply_num","city_id","uid","video","video_object_name","end_time","start_time"});
                activity.put("data",activitys.get(i));
                news.add(activity);
            }
            //2条作品
            List<Map<String,Object>>workss=dao.selectBySQL("select * from "+FSS.works+" where status='1' ORDER BY update_time desc,create_time desc limit "+(Integer.valueOf(pageNum)*2)+",2");
            for (int i = 0; i < workss.size(); i++) {
                if(!Tool.isNull(workss.get(i).get("role"))&&!Tool.isNull(workss.get(i).get("user_id"))&&("manager".equals(workss.get(i).get("role"))||"userapi".equals(workss.get(i).get("role")))){
                    Map<String,Object>works=new HashMap<>();
                    works.put("type",FSS.works+("manager".equals(workss.get(i).get("role"))?"_manager":"_user"));
                    repeatWoksFragment(workss,i);
                    works.put("data",workss.get(i));
                    news.add(works);
                }
            }
            // 1条课堂
            List<Map<String,Object>>classrooms=dao.selectBySQL("select concat('"+FSS.classroom+"') as type,"+FSS.classroom+".* from "+FSS.classroom+" ORDER BY update_time desc,create_time desc limit "+(Integer.valueOf(pageNum)*1)+",1");
            for (int i = 0; i < classrooms.size(); i++) {
                Map<String,Object>classroom=new HashMap<>();
                classroom.put("type",FSS.classroom);
                if(!Tool.isNull(classrooms.get(i).get("column_id"))){
                    classrooms.get(i).put("column_id",getColumnNameByColumnId(classrooms.get(i).get("column_id")));
                }
                if(!Tool.isNull(classrooms.get(i).get("tag_id"))&&Arrays.asList(classrooms.get(i).get("tag_id").toString().trim().split(",")).contains("-2")){
                    classrooms.get(i).put("hot",true);
                }else{
                    classrooms.get(i).put("hot",false);
                }
                Tool.removeMapParmeByKey(classrooms.get(i),new String[]{"images","create_time","description","video","sort","coverphoto","content","start_time","update_time","shor_title","shor_title","user_description","city_id","posters_title","tag_id"});
                classroom.put("data",classrooms.get(i));
                news.add(classroom);
            }
            // 6条资讯,全部按最新修改时间→提交时间排序
            List<Map<String,Object>>informations=dao.selectBySQL("select concat('"+FSS.information+"') as type,"+FSS.information+".* from "+FSS.information+" ORDER BY update_time desc,create_time desc limit "+(Integer.valueOf(pageNum)*6)+",6");
            for (int i = 0; i < informations.size(); i++) {
                if(!Tool.isNull(informations.get(i).get("column_id"))){
                    informations.get(i).put("column_id",getColumnNameByColumnId(informations.get(i).get("column_id")));
                }
                if(!Tool.isNull(informations.get(i).get("tag_id"))&&informations.get(i).get("tag_id").toString().contains("-2")){
                    informations.get(i).put("hot",true);
                }else{
                    informations.get(i).put("hot",false);
                }
                Tool.removeMapParmeByKey(informations.get(i),new String[]{"create_time","content","uid","update_time","publish_ip","source_id","city_id","images","tag_id"});
                Map<String,Object>information=new HashMap<>();
                information.put("type", FSS.information);
                information.put("data",informations.get(i));
                news.add(information);
            }
            Map<String,Object>result=new HashMap<>();
            result.put("hasNext",!Tool.listIsNull(news)&&news.size()>9);
            //1条广告(上面内容加起来一共大于9条,就加一条广告进去,并且要排除掉首页接口一已经用过的两条广告)
            if(!Tool.listIsNull(news)&&news.size()>9){
                List<Map<String,Object>>banners=dao.selectBySQL("SELECT * FROM "+FSS.banner+" AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX(id) FROM "+FSS.banner+")-(SELECT MIN(id) FROM "+FSS.banner+"))+(SELECT MIN(id) FROM "+FSS.banner+")) AS id) AS t2 WHERE t1.id-ROUND(RAND()) >= t2.id and is_ok='1' and type='"+FSS.banner+"' and FIND_IN_SET('-1',tag_id) order by submit_time desc LIMIT "+(Integer.valueOf(pageNum)*1)+",1");
                for (int i = 0; i < banners.size(); i++) {
                    if(!Tool.isNull(banners.get(i).get("tag_id"))&&Arrays.asList(banners.get(i).get("tag_id").toString().trim().split(",")).contains("-2")){
                        banners.get(i).put("hot",true);
                    }else{
                        banners.get(i).put("hot",false);
                    }
                    List<Map<String,Object>>tags=dao.selectBySQL("select * from "+FSS.tag+" where id in("+banners.get(i).get("tag_id")+") and id not in(-1,-2)");
                    List<String>tagNames=new ArrayList<>();
                    for (Map<String, Object> tag : tags) {
                        if(!Tool.isNull(tag.get("name")))tagNames.add(tag.get("name").toString());
                    }
                    banners.get(i).put("tagNames",tagNames);
                    Tool.removeMapParmeByKey(banners.get(i),new String[]{"item_id","submit_time","object_name","id","type","is_ok","tag_id"});
                    Map<String,Object>banner=new HashMap<>();
                    banner.put("type",FSS.banner);
                    banner.put("data",banners.get(i));
                    news.add(banner);
                }
            }
            Collections.shuffle(news);
            result.put("news",news);
            return ResultMsg.success("查询成功",null,result);
        }catch (NumberFormatException nume){
            nume.printStackTrace();
            return  ResultMsg.fail("系统错误","页码数不是一个数字:\n"+ notEmptySQL().toString(),null);
        }catch (Exception e){
            e.printStackTrace();
            return  ResultMsg.fail("系统错误",e.toString(),e);
        }
    }
}
