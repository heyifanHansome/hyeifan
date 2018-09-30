/**
 * 餐厅信息管理管理初始化
 */
var RestaurantInfoManager = {
    id: "RestaurantInfoManagerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
RestaurantInfoManager.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '主键ID', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '餐厅名', field: 'restaurant', visible: true, align: 'center', valign: 'middle'},
            // {title: '餐厅缩略图', field: 'thumb', visible: true, align: 'center', valign: 'middle'},
            // {title: '餐厅图集(无序列表)', field: 'images', visible: true, align: 'center', valign: 'middle'},
            {title: '餐厅所在城市', field: 'cityId', visible: true, align: 'center', valign: 'middle'},
            // {title: '餐厅详细地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            // {title: '餐厅经度', field: 'longitude', visible: true, align: 'center', valign: 'middle'},
            // {title: '餐厅纬度', field: 'latitude', visible: true, align: 'center', valign: 'middle'},
            // {title: '营业时间', field: 'businessHours', visible: true, align: 'center', valign: 'middle'},
            // {title: '加入状态（0：未审查，1：考察，2：通过，3：拒绝）', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
RestaurantInfoManager.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        RestaurantInfoManager.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加餐厅信息管理
 */
RestaurantInfoManager.openAddRestaurantInfoManager = function () {
    var index = layer.open({
        type: 2,
        title: '添加餐厅信息管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/restaurantInfoManager/restaurantInfoManager_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看餐厅信息管理详情
 */
RestaurantInfoManager.openRestaurantInfoManagerDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '餐厅信息管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/restaurantInfoManager/restaurantInfoManager_update/' + RestaurantInfoManager.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除餐厅信息管理
 */
RestaurantInfoManager.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/restaurantInfoManager/delete", function (data) {
            Feng.success("删除成功!");
            RestaurantInfoManager.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("restaurantInfoManagerId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询餐厅信息管理列表
 */
RestaurantInfoManager.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    RestaurantInfoManager.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = RestaurantInfoManager.initColumn();
    var table = new BSTable(RestaurantInfoManager.id, "/restaurantInfoManager/list", defaultColunms);
    table.setPaginationType("client");
    RestaurantInfoManager.table = table.init();
});
