/**
 * 用户资历管理初始化
 */
var ResumeLive = {
    id: "ResumeLiveTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ResumeLive.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键ID', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '简历名称', field: 'userSumerName', visible: true, align: 'center', valign: 'middle'},
            {title: '公司部门', field: 'company', visible: true, align: 'center', valign: 'middle'},
            {title: '职位', field: 'position_name', visible: true, align: 'center', valign: 'middle'},
            {title: '在职起始时间', field: 'start_time', visible: true, align: 'center', valign: 'middle'},
            {title: '在职结束时间', field: 'end_time', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'create_time', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'update_time', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ResumeLive.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ResumeLive.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户资历
 */
ResumeLive.openAddResumeLive = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户资历',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/resumeLive/resumeLive_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户资历详情
 */
ResumeLive.openResumeLiveDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户资历详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/resumeLive/resumeLive_update/' + ResumeLive.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户资历
 */
ResumeLive.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/resumeLive/delete", function (data) {
            Feng.success("删除成功!");
            ResumeLive.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("resumeLiveId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询用户资历列表
 */
ResumeLive.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ResumeLive.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ResumeLive.initColumn();
    var table = new BSTable(ResumeLive.id, "/resumeLive/list", defaultColunms);
    table.setPaginationType("client");
    ResumeLive.table = table.init();
});
