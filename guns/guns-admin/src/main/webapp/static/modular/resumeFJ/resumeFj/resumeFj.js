/**
 * 简历附件管理初始化
 */
var ResumeFj = {
    id: "ResumeFjTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ResumeFj.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '简历ID', field: 'resumeId', visible: true, align: 'center', valign: 'middle'},
            {title: '附件名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '附件地址', field: 'url', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ResumeFj.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ResumeFj.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加简历附件
 */
ResumeFj.openAddResumeFj = function () {
    var index = layer.open({
        type: 2,
        title: '添加简历附件',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/resumeFj/resumeFj_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看简历附件详情
 */
ResumeFj.openResumeFjDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '简历附件详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/resumeFj/resumeFj_update/' + ResumeFj.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除简历附件
 */
ResumeFj.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/resumeFj/delete", function (data) {
            Feng.success("删除成功!");
            ResumeFj.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("resumeFjId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询简历附件列表
 */
ResumeFj.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ResumeFj.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ResumeFj.initColumn();
    var table = new BSTable(ResumeFj.id, "/resumeFj/list", defaultColunms);
    table.setPaginationType("client");
    ResumeFj.table = table.init();
});
