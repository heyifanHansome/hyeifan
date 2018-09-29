/**
 * 用户详情管理初始化
 */
var UserInfo = {
    id: "UserInfoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserInfo.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '自增主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '用户名称', field: 'userId', visible: false, align: 'center', valign: 'middle'},
            {title: '密钥', field: 'api_token', visible: false, align: 'center', valign: 'middle'},
            {title: '积分', field: 'credits', visible: false, align: 'center', valign: 'middle'},
            {title: '账户余额', field: 'money', visible: false, align: 'center', valign: 'middle'},
            {title: '登陆', field: 'login_ip', visible: false, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'create_time', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'update_time', visible: true, align: 'center', valign: 'middle'},
            {title: '实名', field: 'real_name', visible: true, align: 'center', valign: 'middle'},
            {title: '身份证', field: 'id_card', visible: true, align: 'center', valign: 'middle'},
            {title: '城市', field: 'cityId', visible: false, align: 'center', valign: 'middle'},
            {title: '俱乐部', field: 'joinClub', visible: false, align: 'center', valign: 'middle'},
            {title: '预约', field: 'appointment', visible: false, align: 'center', valign: 'middle'},
            {title: '收徒', field: 'enlightening', visible: false, align: 'center', valign: 'middle'},
        {title: '俱乐部', field: 'PjoinClub', visible: true, align: 'center', valign: 'middle'},
        {title: '预约', field: 'Pappointment', visible: true, align: 'center', valign: 'middle'},
        {title: '收徒', field: 'Penlightening', visible: true, align: 'center', valign: 'middle'},
        {title: '用户名称', field: 'Pname', visible: true, align: 'center', valign: 'middle'},
        {title: '余额', field: 'Pmoney', visible: true, align: 'center', valign: 'middle'},
        {title: '登录ip', field: 'PloginIp', visible: true, align: 'center', valign: 'middle'},
        {title: '密钥', field: 'PapiToken', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
UserInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserInfo.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户详情
 */
UserInfo.openAddUserInfo = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户详情',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userInfo/userInfo_add'
    });
    this.layerIndex = index;
};

/*打开用户简历弹框*/
UserInfo.getUserResume = function(){
    debugger;
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户详情详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userResume/userinfouserResume_update/' + UserInfo.seItem.id
        });
        this.layerIndex = index;
    }
}

/**
 * 打开查看用户详情详情
 */
UserInfo.openUserInfoDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户详情详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userInfo/userInfo_update/' + UserInfo.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户详情
 */
UserInfo.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/userInfo/delete", function (data) {
            Feng.success("删除成功!");
            UserInfo.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userInfoId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询用户详情列表
 */
UserInfo.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    UserInfo.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserInfo.initColumn();
    var table = new BSTable(UserInfo.id, "/userInfo/list", defaultColunms);
    table.setPaginationType("client");
    UserInfo.table = table.init();
});
