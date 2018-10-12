/**
 * 评论管理管理初始化
 */
var UserComment = {
    id: "UserCommentTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserComment.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '自增主键（评论ID）', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '栏目', field: 'columnName', visible: true, align: 'center', valign: 'middle'},
            {title: '被评论文章标题', field: 'itemTitle', visible: true, align: 'center', valign: 'middle'},
            {title: '评论内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
            // {title: '该条记录所回复的评论ID,没有则填0', field: 'commentId', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
UserComment.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserComment.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加评论管理
 */
UserComment.openAddUserComment = function () {
    var index = layer.open({
        type: 2,
        title: '添加评论管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userComment/userComment_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看评论管理详情
 */
UserComment.openUserCommentDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '评论管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userComment/userComment_update/' + UserComment.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除评论管理
 */
UserComment.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/userComment/delete", function (data) {
            Feng.success("删除成功!");
            UserComment.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userCommentId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询评论管理列表
 */
UserComment.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    UserComment.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserComment.initColumn();
    var table = new BSTable(UserComment.id, "/userComment/list", defaultColunms);
    table.setPaginationType("client");
    UserComment.table = table.init();
});
