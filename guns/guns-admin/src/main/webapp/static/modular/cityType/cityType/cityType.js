/**
 * 城市分类管理初始化
 */
var CityType = {
    id: "CityTypeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
CityType.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '自增主键', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '分类名（如一线城市、二线城市、三线城市）', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updatedTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
CityType.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        CityType.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加城市分类
 */
CityType.openAddCityType = function () {
    var index = layer.open({
        type: 2,
        title: '添加城市分类',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/cityType/cityType_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看城市分类详情
 */
CityType.openCityTypeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '城市分类详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/cityType/cityType_update/' + CityType.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除城市分类
 */
CityType.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/cityType/delete", function (data) {
            Feng.success("删除成功!");
            CityType.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("cityTypeId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询城市分类列表
 */
CityType.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    CityType.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = CityType.initColumn();
    var table = new BSTable(CityType.id, "/cityType/list", defaultColunms);
    table.setPaginationType("client");
    CityType.table = table.init();
});
