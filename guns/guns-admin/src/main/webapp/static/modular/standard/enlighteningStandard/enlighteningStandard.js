/**
 * 厨师收徒标准管理初始化
 */
var EnlighteningStandard = {
    id: "EnlighteningStandardTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
EnlighteningStandard.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键ID', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户id(师傅)', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '等级ID', field: 'gradeId', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
            {title: '出生日期（年龄）', field: 'birthday', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '结束时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
EnlighteningStandard.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        EnlighteningStandard.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加厨师收徒标准
 */
EnlighteningStandard.openAddEnlighteningStandard = function () {
    var index = layer.open({
        type: 2,
        title: '添加厨师收徒标准',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/enlighteningStandard/enlighteningStandard_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看厨师收徒标准详情
 */
EnlighteningStandard.openEnlighteningStandardDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '厨师收徒标准详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/enlighteningStandard/enlighteningStandard_update/' + EnlighteningStandard.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除厨师收徒标准
 */
EnlighteningStandard.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/enlighteningStandard/delete", function (data) {
            Feng.success("删除成功!");
            EnlighteningStandard.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("enlighteningStandardId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询厨师收徒标准列表
 */
EnlighteningStandard.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    EnlighteningStandard.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = EnlighteningStandard.initColumn();
    var table = new BSTable(EnlighteningStandard.id, "/enlighteningStandard/list", defaultColunms);
    table.setPaginationType("client");
    EnlighteningStandard.table = table.init();
});
