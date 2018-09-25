/**
 * 栏目分类管理初始化
 */
var ColumnType = {
    id: "ColumnTypeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ColumnType.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: 'ID', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '父级栏目', field: 'parentId', visible: true, align: 'center', valign: 'middle'},
            {title: '排序', field: 'orders', visible: true, align: 'center', valign: 'middle'},
            {title: '栏目类型名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ColumnType.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ColumnType.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加栏目分类
 */
ColumnType.openAddColumnType = function () {
    var index = layer.open({
        type: 2,
        title: '添加栏目分类',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/columnType/columnType_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看栏目分类详情
 */
ColumnType.openColumnTypeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '栏目分类详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/columnType/columnType_update/' + ColumnType.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除栏目分类
 */
ColumnType.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/columnType/delete", function (data) {
            Feng.success("删除成功!");
            ColumnType.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("columnTypeId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询栏目分类列表
 */
ColumnType.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ColumnType.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ColumnType.initColumn();
    var table = new BSTable(ColumnType.id, "/columnType/list", defaultColunms);
    table.setPaginationType("client");
    ColumnType.table = table.init();
});
