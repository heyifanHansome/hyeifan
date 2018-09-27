/**
 * 星厨俱乐部管理初始化
 */
var Club = {
    id: "ClubTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Club.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '自增主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '用户', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '等级ID', field: 'gradeId', visible: true, align: 'center', valign: 'middle'},
            {title: '实名', field: 'realName', visible: true, align: 'center', valign: 'middle'},
            {title: '身份证号', field: 'idCard', visible: true, align: 'center', valign: 'middle'},
            {title: '加入状态（0：未审查，1：考察，2：通过，3：拒绝）', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Club.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Club.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加星厨俱乐部
 */
Club.openAddClub = function () {
    var index = layer.open({
        type: 2,
        title: '添加星厨俱乐部',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/club/club_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看星厨俱乐部详情
 */
Club.openClubDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '星厨俱乐部详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/club/club_update/' + Club.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除星厨俱乐部
 */
Club.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/club/delete", function (data) {
            Feng.success("删除成功!");
            Club.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("clubId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询星厨俱乐部列表
 */
Club.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Club.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Club.initColumn();
    var table = new BSTable(Club.id, "/club/list", defaultColunms);
    table.setPaginationType("client");
    Club.table = table.init();
});
