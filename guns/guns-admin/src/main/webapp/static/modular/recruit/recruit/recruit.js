/**
 * 招聘管理管理初始化
 */
var Recruit = {
    id: "RecruitTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Recruit.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '自增主键', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '栏目类型ID为招聘的ID', field: 'columnId', visible: true, align: 'center', valign: 'middle'},
            {title: '招聘名', field: 'title', visible: true, align: 'center', valign: 'middle'},
            {title: '缩略图', field: 'thumb', visible: true, align: 'center', valign: 'middle'},
            {title: '描述', field: 'description', visible: true, align: 'center', valign: 'middle'},
            {title: '城市ID（0：表示全国）', field: 'cityId', visible: true, align: 'center', valign: 'middle'},
            {title: '工作详细地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            {title: '来源ID（0：官方，1：个人）', field: 'sourceId', visible: true, align: 'center', valign: 'middle'},
            {title: '当是官方时为管理员的ID，是个人时，表示用户的ID', field: 'uid', visible: true, align: 'center', valign: 'middle'},
            {title: '发布IP', field: 'publishIp', visible: true, align: 'center', valign: 'middle'},
            {title: '内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updatedTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Recruit.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Recruit.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加招聘管理
 */
Recruit.openAddRecruit = function () {
    var index = layer.open({
        type: 2,
        title: '添加招聘管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/recruit/recruit_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看招聘管理详情
 */
Recruit.openRecruitDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '招聘管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/recruit/recruit_update/' + Recruit.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除招聘管理
 */
Recruit.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/recruit/delete", function (data) {
            Feng.success("删除成功!");
            Recruit.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("recruitId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询招聘管理列表
 */
Recruit.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Recruit.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Recruit.initColumn();
    var table = new BSTable(Recruit.id, "/recruit/list", defaultColunms);
    table.setPaginationType("client");
    Recruit.table = table.init();
});
