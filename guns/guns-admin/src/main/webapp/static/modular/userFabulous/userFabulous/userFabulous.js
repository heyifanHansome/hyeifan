/**
 * 用户点赞管理初始化
 */
var UserFabulous = {
    id: "UserFabulousTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserFabulous.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户ID', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '栏目类型ID（当为作品类型时，works_id表示作品ID, 当为资讯类型时，works_id表示资讯ID）', field: 'columnId', visible: true, align: 'center', valign: 'middle'},
            {title: '作品（资讯、课堂、活动）ID', field: 'worksId', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
UserFabulous.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserFabulous.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户点赞
 */
UserFabulous.openAddUserFabulous = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户点赞',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userFabulous/userFabulous_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户点赞详情
 */
UserFabulous.openUserFabulousDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户点赞详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userFabulous/userFabulous_update/' + UserFabulous.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户点赞
 */
UserFabulous.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/userFabulous/delete", function (data) {
            Feng.success("删除成功!");
            UserFabulous.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userFabulousId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询用户点赞列表
 */
UserFabulous.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    UserFabulous.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserFabulous.initColumn();
    var table = new BSTable(UserFabulous.id, "/userFabulous/list", defaultColunms);
    table.setPaginationType("client");
    UserFabulous.table = table.init();
});
