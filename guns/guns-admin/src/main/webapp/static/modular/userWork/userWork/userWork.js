/**
 * 用户作品管理初始化
 */
var UserWork = {
    id: "UserWorkTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserWork.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '角色', field: 'Prole', visible: true, align: 'center', valign: 'middle'},
            {title: '用户', field: 'Pname', visible: true, align: 'center', valign: 'middle'},
            {title: '作品', field: 'Pworks', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'create_time', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'update_time', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
UserWork.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserWork.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户作品
 */
UserWork.openAddUserWork = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户作品',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userWork/userWork_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户作品详情
 */
UserWork.openUserWorkDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户作品详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userWork/userWork_update/' + UserWork.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户作品
 */
UserWork.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/userWork/delete", function (data) {
            Feng.success("删除成功!");
            UserWork.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userWorkId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询用户作品列表
 */
UserWork.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    UserWork.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserWork.initColumn();
    var table = new BSTable(UserWork.id, "/userWork/list", defaultColunms);
    table.setPaginationType("client");
    UserWork.table = table.init();
});
