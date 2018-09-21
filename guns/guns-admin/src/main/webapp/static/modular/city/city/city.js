/**
 * 城市管理管理初始化
 */
var City = {
    id: "CityTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
City.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '自增主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '城市分类', field: 'typeId', visible: false, align: 'center', valign: 'middle'},
            {title: '城市名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: false, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: false, align: 'center', valign: 'middle'},
            {title: '城市名称', field: 'cityTypeName', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'update_time', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'update_time', visible: true, align: 'center', valign: 'middle'},

    ];
};

/**
 * 检查是否选中
 */
City.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        City.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加城市管理
 */
City.openAddCity = function () {
    var index = layer.open({
        type: 2,
        title: '添加城市管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/city/city_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看城市管理详情
 */
City.openCityDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '城市管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/city/city_update/' + City.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除城市管理
 */
City.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/city/delete", function (data) {
            Feng.success("删除成功!");
            City.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("cityId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询城市管理列表
 */
City.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    City.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = City.initColumn();
    var table = new BSTable(City.id, "/city/list", defaultColunms);
    table.setPaginationType("client");
    City.table = table.init();
});
