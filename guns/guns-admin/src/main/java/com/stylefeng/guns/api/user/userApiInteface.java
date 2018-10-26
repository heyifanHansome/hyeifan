package com.stylefeng.guns.api.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.lijun.util.*;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.model.UserInfo;
import com.stylefeng.guns.modular.system.model.UserTarget;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import com.stylefeng.guns.modular.userTarget.service.IUserTargetService;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private IUserTargetService userTargetService;
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
    private List<Map<String, Object>> getUser(String phone,String token){
        return dao.selectBySQL("SELECT api.*,info.id info_id,info.user_id,info.api_token,info.credits,info.money,info.login_ip,info.create_time,info.update_time,info.real_name,info.id_card,info.city_id,info.join_club,info.appointment,info.enlightening FROM "+FSS.user_api+" api RIGHT JOIN "+FSS.user_info+" info ON (api.id = info.user_id) WHERE api.phone = '"+phone+"' AND info.api_token = '"+token+"'");
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
    @ApiResponses(@ApiResponse(code = 200, message = "data:{targets:" +
            "[" + "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
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
            "  ],"+ "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "  userTargetIds:[" +"</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "用户提交过审核的(没提交过审核的,你传了用户id这里就是个空数组)第三层(选项)对应的ID数组"+"</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "]" +"</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "  }"+ "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
            "注意:用户在选择完之后,要以同样格式!要以同样格式!要以同样格式!将三级,也就是最里面\"选项\"有选中的包装起来提交,三级没有选中的,该三级所在的二级就不用包装,如果同一个一级下的二级都没有选中三级,那这个一级也不用包装给我,最后是JSON字符串形式提交"))
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



    @RequestMapping(value = "indexPartStatic",method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg indexPartStatic(Double lng_x,Double lat_y){
        try{
            Map<String,Object>result=new HashMap<>();
            //开始获取推荐厨师
            //1.拉取厨师分组的所有城市ID,用这些城市ID找出城市集合
            List<String>cityIds=new ArrayList<>();
            List<Map<String,Object>>groupUsers=dao.selectBySQL("select * from "+FSS.index_up_group_user+" where city_id<>0 and "+ notEmptySQL("city_id"));
            for (Map<String, Object> groupUser : groupUsers) {
                cityIds.add(groupUser.get("city_id").toString());
            }
            List<Map<String,Object>>citys=dao.selectBySQL("select * from "+FSS.city+" where id in("+ StringUtils.join(cityIds,",")+") and"+notEmptySQL("lngx","laty"));
            //2.在城市集合中,用前台传进来的lng_x和lat_y两个坐标找出距离最近的城市,的ID
            Map<String,Object>city=null;
            double tempDistanceOne=Double.MAX_VALUE;
            for (int i = 0; i < citys.size(); i++) {
                double tempDistanceTwo=LocationUtils.getDistance(lat_y,lng_x,Double.valueOf(citys.get(i).get("laty").toString()),Double.valueOf(citys.get(i).get("lngx").toString()));
                if(tempDistanceTwo<tempDistanceOne){
                    tempDistanceOne=tempDistanceTwo;
                    city=citys.get(i);
                }
            }
            //3.用最近的城市的id找到对应分组的厨师们,再加上城市ID等于0也就是分组为"全国"的厨师们,放到要返回的结果result里去(这里要特别注意顺序一定要符合数据库里用户的ID顺序)
            LinkedHashSet<String>userIDs=new LinkedHashSet<>();
            if(city!=null){
                List<Map<String,Object>>groups=dao.selectBySQL("select * from "+FSS.index_up_group_user+" where city_id='"+city.get("id")+"'");
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
                //去除没必要的字段
            }
            result.put("chefs",userApis);

            //开始放入跳转板块
            List<Map<String,Object>>jumpPages=dao.selectBySQL("select * from "+FSS.jump_page+" order by orders asc");
            for (int i = 0; i < jumpPages.size(); i++) {
                //去除没必要的字段
            }
            result.put("jumpPages",jumpPages);

            //开始放入加精内容(资讯/课堂按照 首页加精→最新修改时间排序 活动/作品(用户-餐厅)按照 定位最近→最新修改时间排序)
            //1活动,3资讯,1课堂,3资讯,1用户作品,3资讯,1餐厅作品,3资讯
            //每个map有一个type字段,用以区分展示的是大图小图
            List<Map<String,Object>>news=new ArrayList<>();


            return ResultMsg.success("查询成功",null,result);
        }catch (Exception e){
            e.printStackTrace();
            return  ResultMsg.fail("系统错误",e.toString(),null);
        }
    }

}
