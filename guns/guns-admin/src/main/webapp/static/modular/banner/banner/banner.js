/**
 * 广告管理管理初始化
 */
var Banner = {
    id: "BannerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Banner.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
        {title: '封面图片', field: 'picture',visible: true, align: 'center', valign: 'middle',class:"img"},
            // {title: '阿里云OSS删除时用到的key', field: 'object_name', visible: true, align: 'center', valign: 'middle'},
            // {title: '广告跳转的超链接', field: 'href', visible: true, align: 'center', valign: 'middle'},
            {title: '标签', field: 'tagId', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Banner.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Banner.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加广告管理
 */
Banner.openAddBanner = function () {
    var index = layer.open({
        type: 2,
        title: '添加广告管理',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/banner/banner_add' + '?menu_id='+location.search.split("=")[location.search.split("=").length-1]
    });
    this.layerIndex = index;
};

/**
 * 打开查看广告管理详情
 */
Banner.openBannerDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '广告管理详情',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/banner/banner_update/' + Banner.seItem.id + '?menu_id='+location.search.split("=")[location.search.split("=").length-1]
        });
        this.layerIndex = index;
    }
};

/**
 * 删除广告管理
 */
Banner.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/banner/delete", function (data) {
            Feng.success("删除成功!");
            Banner.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bannerId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询广告管理列表
 */
Banner.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Banner.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Banner.initColumn();
    var table = new BSTable(Banner.id, "/banner/list", defaultColunms);
    table.setPaginationType("client");
    Banner.table = table.init();

});
