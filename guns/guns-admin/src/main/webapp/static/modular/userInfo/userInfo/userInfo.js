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
            {title: '自增主键', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户ID', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '密钥', field: 'apiToken', visible: true, align: 'center', valign: 'middle'},
            {title: '积分', field: 'credits', visible: true, align: 'center', valign: 'middle'},
            {title: '钱', field: 'money', visible: true, align: 'center', valign: 'middle'},
            {title: '登陆IP', field: 'loginIp', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '结束时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
            {title: '实名', field: 'realName', visible: true, align: 'center', valign: 'middle'},
            {title: '身份证', field: 'idCard', visible: true, align: 'center', valign: 'middle'},
            {title: '城市ID，当为0时表示是全国', field: 'cityId', visible: true, align: 'center', valign: 'middle'},
            {title: '是否加入俱乐部(0：否，1：是)', field: 'joinClub', visible: true, align: 'center', valign: 'middle'},
            {title: '是否开启预约(0：否，1：是)', field: 'appointment', visible: true, align: 'center', valign: 'middle'},
            {title: '是否开启收徒(0：否，1：是)', field: 'enlightening', visible: true, align: 'center', valign: 'middle'}
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
