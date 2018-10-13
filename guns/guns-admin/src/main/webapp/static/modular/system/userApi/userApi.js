/**
 * 管理初始化
 */
var UserApi = {
    id: "UserApiTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserApi.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '用户自增主键id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '头像', field: 'avatar', visible: false, align: 'center', valign: 'middle'},
            {title: '账号', field: 'account', visible: false, align: 'center', valign: 'middle'},
            {title: '密码', field: 'password', visible: false, align: 'center', valign: 'middle'},
            {title: 'md5密码盐', field: 'salt', visible: false, align: 'center', valign: 'middle'},
            {title: '名字', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '生日', field: 'birthday', visible: true, align: 'center', valign: 'middle'},
            {title: '性别', field: 'sexName', visible: true, align: 'center', valign: 'middle'},
            {title: '电子邮件', field: 'email', visible: false, align: 'center', valign: 'middle'},
            {title: '电话', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updatetime', visible: true, align: 'center', valign: 'middle'}
    ];
};


/**
 * 点击打开用户详情模态框
 * @param
 */
UserApi.userInfoDetail = function () {

    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户详情',
            area: ['800px','560px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userLoginApi/user_info/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};


/**
 * 检查是否选中
 */
UserApi.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserApi.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
UserApi.openAddUserApi = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userLoginApi/userApi_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
UserApi.openUserApiDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userLoginApi/userApi_update/' + UserApi.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
UserApi.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/userLoginApi/delete", function (data) {
            Feng.success("删除成功!");
            UserApi.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userApiId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
UserApi.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    UserApi.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserApi.initColumn();
    var table = new BSTable(UserApi.id, "/userLoginApi/list", defaultColunms);
    table.setPaginationType("client");
    UserApi.table = table.init();
});
