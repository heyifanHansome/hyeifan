/**
 * 会员等级审核管理初始化
 */
var UserTarget = {
    id: "UserTargetTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserTarget.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户', field: 'uid', visible: true, align: 'center', valign: 'middle'},
            // {title: '评级指标的JSONArray内容', field: 'target', visible: true, align: 'center', valign: 'middle'},
            {title: '会员等级', field: 'lv', visible: true, align: 'center', valign: 'middle'},
            {title: '是否审核', field: 'check', visible: true, align: 'center', valign: 'middle'},
            // {title: '材料补充,格式:[{"name":"xxxx","data":["x.jpg","y.png"]}]', field: 'replenish', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'updatetime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
UserTarget.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserTarget.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员等级审核
 */
UserTarget.openAddUserTarget = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员等级审核',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userTarget/userTarget_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员等级审核详情
 */
UserTarget.openUserTargetDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员等级审核详情',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userTarget/userTarget_update/' + UserTarget.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员等级审核
 */
UserTarget.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/userTarget/delete", function (data) {
            Feng.success("删除成功!");
            UserTarget.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userTargetId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员等级审核列表
 */
UserTarget.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    UserTarget.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserTarget.initColumn();
    var table = new BSTable(UserTarget.id, "/userTarget/list", defaultColunms);
    table.setPaginationType("client");
    UserTarget.table = table.init();
});
