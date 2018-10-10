/**
 * 作品管理管理初始化
 */
var Works = {
    id: "WorksTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Works.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键ID', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '作品名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '资源', field: 'images', visible: false, align: 'center', valign: 'middle'},
            {title: '主料', field: 'mainIngredient', visible: false, align: 'center', valign: 'middle'},
            {title: '辅料', field: 'supplementaryMaterial', visible: false, align: 'center', valign: 'middle'},
            {title: '调料', field: 'seasoning', visible: false, align: 'center', valign: 'middle'},
            {title: '做法', field: 'practice', visible: false, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
            {title: '加入状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'create_time', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'update_time', visible: true, align: 'center', valign: 'middle'},
            {title: '栏目类型', field: 'columnTypeName', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Works.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Works.seItem = selected[0];
        return true;
    }
};

Works.heyifan = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '作品管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/works/works_update/' + Works.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 点击添加作品管理
 */
Works.openAddWorks = function () {
    var index = layer.open({
        type: 2,
        title: '添加作品管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/works/works_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看作品管理详情
 */
Works.openWorksDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '作品管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/works/works_update/' + Works.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除作品管理
 */
Works.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/works/delete", function (data) {
            Feng.success("删除成功!");
            Works.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("worksId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询作品管理列表
 */
Works.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Works.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Works.initColumn();
    var table = new BSTable(Works.id, "/works/list", defaultColunms);
    table.setPaginationType("client");
    Works.table = table.init();



});
