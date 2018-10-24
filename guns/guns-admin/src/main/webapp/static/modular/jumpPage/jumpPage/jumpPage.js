/**
 * 首页跳转模块管理初始化
 */
var JumpPage = {
    id: "JumpPageTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
JumpPage.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '背景图', field: 'picture', visible: true, align: 'center', valign: 'middle',class:'img'},
            // {title: '阿里云OSS删除的key', field: 'objectName', visible: true, align: 'center', valign: 'middle'},
            {title: '跳转页面', field: 'code', visible: true, align: 'center', valign: 'middle'},
        {title: '排序值', field: 'orders', visible: true, align: 'center', valign: 'middle'},
        {title: '是否启用', field: 'enable', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
JumpPage.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        JumpPage.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加首页跳转模块
 */
JumpPage.openAddJumpPage = function () {
    var index = layer.open({
        type: 2,
        title: '添加首页跳转模块',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/jumpPage/jumpPage_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看首页跳转模块详情
 */
JumpPage.openJumpPageDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '首页跳转模块详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/jumpPage/jumpPage_update/' + JumpPage.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除首页跳转模块
 */
JumpPage.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/jumpPage/delete", function (data) {
            Feng.success("删除成功!");
            JumpPage.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("jumpPageId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询首页跳转模块列表
 */
JumpPage.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    JumpPage.table.refresh({query: queryData});
};

$(function () {
    $.ajaxSetup({
        complete:function () {
            $('.img').each(function (i) {
                if($(this).text().startsWith("http")){
                    var imgUrl=$(this).text();
                    $(this).empty().append('<img src="'+imgUrl+'" style="width: 100%;" />');
                }
            });
        }
    });
    var defaultColunms = JumpPage.initColumn();
    var table = new BSTable(JumpPage.id, "/jumpPage/list", defaultColunms);
    table.setPaginationType("client");
    JumpPage.table = table.init();
});
