/**
 * 标签管理表管理初始化
 */
var Tag = {
    id: "TagTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Tag.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '栏目类型', field: 'columnId', visible: true, align: 'center', valign: 'middle'},
        {title: '标签名', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '图片', field: 'picture', visible: true, align: 'center', valign: 'middle',class:'img'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Tag.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Tag.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加标签管理表
 */
Tag.openAddTag = function () {
    var index = layer.open({
        type: 2,
        title: '添加标签管理表',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/tag/tag_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看标签管理表详情
 */
Tag.openTagDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '标签管理表详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/tag/tag_update/' + Tag.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除标签管理表
 */
Tag.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/tag/delete", function (data) {
            Feng.success("删除成功!");
            Tag.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("tagId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询标签管理表列表
 */
Tag.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Tag.table.refresh({query: queryData});
};

$(function () {
    $.ajaxSetup({
        complete:function () {
            $('.img').each(function (i) {
                if(i>1){
                    var imgUrl=$(this).text();
                    $(this).empty().append('<img src="'+imgUrl+'" style="width: 100%;" />');
                }
            });
        }
    });
    var defaultColunms = Tag.initColumn();
    var table = new BSTable(Tag.id, "/tag/list", defaultColunms);
    table.setPaginationType("client");
    Tag.table = table.init();
});
