/**
 * 资讯管理管理初始化
 */
var Information = {
    id: "InformationTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Information.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '自增主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '栏目类型', field: 'columnTypeName', visible: true, align: 'center', valign: 'middle'},
            {title: '标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
            {title: '缩略图', field: 'thumb', visible: true, align: 'center', valign: 'middle'},
            {title: '图集', field: 'images', visible: true, align: 'center', valign: 'middle'},
            {title: '视频地址', field: 'url', visible: true, align: 'center', valign: 'middle'},
            {title: '描述', field: 'description', visible: true, align: 'center', valign: 'middle'},
            {title: '城市', field: 'cityName', visible: true, align: 'center', valign: 'middle'},
            {title: '来源', field: 'sourceName', visible: true, align: 'center', valign: 'middle'},
            {title: '发布人', field: 'userName', visible: true, align: 'center', valign: 'middle'},
            {title: '发布IP', field: 'publish_ip', visible: true, align: 'center', valign: 'middle'},
            {title: '内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'create_time', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'update_time', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Information.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Information.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加资讯管理
 */
Information.openAddInformation = function () {
    var index = layer.open({
        type: 2,
        title: '添加资讯管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/information/information_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看资讯管理详情
 */
Information.openInformationDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '资讯管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/information/information_update/' + Information.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除资讯管理
 */
Information.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/information/delete", function (data) {
            Feng.success("删除成功!");
            Information.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("informationId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询资讯管理列表
 */
Information.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Information.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Information.initColumn();
    var table = new BSTable(Information.id, "/information/list", defaultColunms);
    table.setPaginationType("client");
    Information.table = table.init();
});
