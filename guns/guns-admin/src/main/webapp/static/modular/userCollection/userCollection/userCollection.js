/**
 * 用户收藏管理管理初始化
 */
var UserCollection = {
    id: "UserCollectionTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserCollection.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'ID', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户ID', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '栏目类型ID（当为作品类型时，works_id表示作品ID, 当为资讯类型时，works_id表示资讯ID）', field: 'columnId', visible: true, align: 'center', valign: 'middle'},
            {title: '作品（资讯、课堂、活动）ID', field: 'worksId', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
UserCollection.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserCollection.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户收藏管理
 */
UserCollection.openAddUserCollection = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户收藏管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userCollection/userCollection_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户收藏管理详情
 */
UserCollection.openUserCollectionDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户收藏管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userCollection/userCollection_update/' + UserCollection.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户收藏管理
 */
UserCollection.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/userCollection/delete", function (data) {
            Feng.success("删除成功!");
            UserCollection.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userCollectionId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询用户收藏管理列表
 */
UserCollection.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    UserCollection.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserCollection.initColumn();
    var table = new BSTable(UserCollection.id, "/userCollection/list", defaultColunms);
    table.setPaginationType("client");
    UserCollection.table = table.init();
});
