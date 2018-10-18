/**
 * 活动报名管理管理初始化
 */
var ActivityApply = {
    id: "ActivityApplyTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ActivityApply.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '报名表id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '报名活动', field: 'activityId', visible: true, align: 'center', valign: 'middle'},
            {title: '已报名人数', field: 'userApiId', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ActivityApply.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ActivityApply.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加活动报名管理
 */
ActivityApply.openAddActivityApply = function () {
    var index = layer.open({
        type: 2,
        title: '添加活动报名管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/activityApply/activityApply_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看活动报名管理详情
 */
ActivityApply.openActivityApplyDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '活动报名管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/activityApply/activityApply_update/' + ActivityApply.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除活动报名管理
 */
ActivityApply.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/activityApply/delete", function (data) {
            Feng.success("删除成功!");
            ActivityApply.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("activityApplyId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询活动报名管理列表
 */
ActivityApply.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ActivityApply.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ActivityApply.initColumn();
    var table = new BSTable(ActivityApply.id, "/activityApply/list", defaultColunms);
    table.setPaginationType("client");
    ActivityApply.table = table.init();
});
