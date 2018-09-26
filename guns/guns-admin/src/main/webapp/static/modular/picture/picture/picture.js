/**
 * 图片管理管理初始化
 */
var Picture = {
    id: "PictureTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Picture.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: ' 创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '创建人 ', field: 'createBy', visible: true, align: 'center', valign: 'middle'},
            {title: '外键id', field: 'baseId', visible: true, align: 'center', valign: 'middle'},
            {title: '图片名称', field: 'picturename', visible: true, align: 'center', valign: 'middle'},
            {title: '相对路径', field: 'relativepath', visible: true, align: 'center', valign: 'middle'},
            {title: '服务器路径', field: 'serverpath', visible: true, align: 'center', valign: 'middle'},
            {title: '绝对路径', field: 'absolutepath', visible: true, align: 'center', valign: 'middle'},
            {title: '类型', field: 'type', visible: true, align: 'center', valign: 'middle'},
            {title: '后缀', field: 'suffixname', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Picture.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Picture.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加图片管理
 */
Picture.openAddPicture = function () {
    var index = layer.open({
        type: 2,
        title: '添加图片管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/picture/picture_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看图片管理详情
 */
Picture.openPictureDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '图片管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/picture/picture_update/' + Picture.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除图片管理
 */
Picture.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/picture/delete", function (data) {
            Feng.success("删除成功!");
            Picture.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("pictureId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询图片管理列表
 */
Picture.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Picture.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Picture.initColumn();
    var table = new BSTable(Picture.id, "/picture/list", defaultColunms);
    table.setPaginationType("client");
    Picture.table = table.init();
});
