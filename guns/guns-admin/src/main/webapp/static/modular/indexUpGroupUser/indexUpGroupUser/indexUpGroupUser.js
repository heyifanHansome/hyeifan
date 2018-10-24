/**
 * 厨师推荐分组管理管理初始化
 */
var IndexUpGroupUser = {
    id: "IndexUpGroupUserTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
IndexUpGroupUser.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '城市', field: 'cityId', visible: true, align: 'center', valign: 'middle'},
            {title: '人数', field: 'userApiId', visible: true, align: 'center', valign: 'middle'},
            {title: '提交时间', field: 'submitTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
IndexUpGroupUser.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        IndexUpGroupUser.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加厨师推荐分组管理
 */
IndexUpGroupUser.openAddIndexUpGroupUser = function () {
    var index = layer.open({
        type: 2,
        title: '添加厨师推荐分组管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/indexUpGroupUser/indexUpGroupUser_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看厨师推荐分组管理详情
 */
IndexUpGroupUser.openIndexUpGroupUserDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '厨师推荐分组管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/indexUpGroupUser/indexUpGroupUser_update/' + IndexUpGroupUser.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除厨师推荐分组管理
 */
IndexUpGroupUser.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/indexUpGroupUser/delete", function (data) {
            Feng.success("删除成功!");
            IndexUpGroupUser.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("indexUpGroupUserId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询厨师推荐分组管理列表
 */
IndexUpGroupUser.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    IndexUpGroupUser.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = IndexUpGroupUser.initColumn();
    var table = new BSTable(IndexUpGroupUser.id, "/indexUpGroupUser/list", defaultColunms);
    table.setPaginationType("client");
    IndexUpGroupUser.table = table.init();
});
