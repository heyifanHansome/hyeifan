/**
 * 标签关联管理初始化
 */
var TagRelation = {
    id: "TagRelationTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
TagRelation.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'ID', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '栏目类型ID（当为0时表示通用标签）', field: 'columnId', visible: true, align: 'center', valign: 'middle'},
            {title: '公共标签ID（单选）', field: 'commonTypeId', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
            {title: '关联ID（可能一样，来源作品表、课堂表、活动表、资讯表等）', field: 'relationId', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
TagRelation.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        TagRelation.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加标签关联
 */
TagRelation.openAddTagRelation = function () {
    var index = layer.open({
        type: 2,
        title: '添加标签关联',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/tagRelation/tagRelation_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看标签关联详情
 */
TagRelation.openTagRelationDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '标签关联详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/tagRelation/tagRelation_update/' + TagRelation.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除标签关联
 */
TagRelation.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/tagRelation/delete", function (data) {
            Feng.success("删除成功!");
            TagRelation.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("tagRelationId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询标签关联列表
 */
TagRelation.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    TagRelation.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = TagRelation.initColumn();
    var table = new BSTable(TagRelation.id, "/tagRelation/list", defaultColunms);
    table.setPaginationType("client");
    TagRelation.table = table.init();
});
