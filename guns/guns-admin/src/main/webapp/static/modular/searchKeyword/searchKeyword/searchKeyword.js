/**
 * 搜索关键字管理管理初始化
 */
var SearchKeyword = {
    id: "SearchKeywordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SearchKeyword.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '关键词内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            {title: '命中次数', field: 'hit_num', visible: true, align: 'center', valign: 'middle'},
        {title: '排序值', field: 'orders', visible: true, align: 'center', valign: 'middle'},
        {title: '保存时间', field: 'submit_time', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
SearchKeyword.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        SearchKeyword.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加搜索关键字管理
 */
SearchKeyword.openAddSearchKeyword = function () {
    var index = layer.open({
        type: 2,
        title: '添加搜索关键字管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/searchKeyword/searchKeyword_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看搜索关键字管理详情
 */
SearchKeyword.openSearchKeywordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '搜索关键字管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/searchKeyword/searchKeyword_update/' + SearchKeyword.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除搜索关键字管理
 */
SearchKeyword.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/searchKeyword/delete", function (data) {
            Feng.success("删除成功!");
            SearchKeyword.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("searchKeywordId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询搜索关键字管理列表
 */
SearchKeyword.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    SearchKeyword.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SearchKeyword.initColumn();
    var table = new BSTable(SearchKeyword.id, "/searchKeyword/list", defaultColunms);
    table.setPaginationType("client");
    SearchKeyword.table = table.init();
});
