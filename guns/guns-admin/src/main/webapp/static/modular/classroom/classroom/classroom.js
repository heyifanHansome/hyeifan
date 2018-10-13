/**
 * 星厨课堂管理初始化
 */
var Classroom = {
    id: "ClassroomTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Classroom.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '自增主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '栏目类型', field: 'columnTypeName', visible: true, align: 'center', valign: 'middle'},
            {title: '标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
            {title: '缩略图', field: 'thumb', visible: true, align: 'center', valign: 'middle'},
            {title: '视频链接地址', field: 'url', visible: false, align: 'center', valign: 'middle'},
            {title: '描述', field: 'description', visible: true, align: 'center', valign: 'middle'},
            {title: '开播时间', field: 'start_time', visible: true, align: 'center', valign: 'middle'},
            {title: '城市', field: 'cityName', visible: true, align: 'center', valign: 'middle'},
            {title: '来源', field: 'sourceName', visible: true, align: 'center', valign: 'middle'},
            {title: '发布者', field: 'userName', visible: false, align: 'center', valign: 'middle'},
            {title: '发布IP', field: 'publish_ip', visible: false, align: 'center', valign: 'middle'},
            {title: '内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'create_time', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'update_time', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Classroom.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Classroom.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加星厨课堂
 */
Classroom.openAddClassroom = function () {
    var index = layer.open({
        type: 2,
        title: '添加星厨课堂',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/classroom/classroom_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看星厨课堂详情
 */
Classroom.openClassroomDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '星厨课堂详情',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/classroom/classroom_update/' + Classroom.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除星厨课堂
 */
Classroom.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/classroom/delete", function (data) {
            Feng.success("删除成功!");
            Classroom.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("classroomId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询星厨课堂列表
 */
Classroom.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Classroom.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Classroom.initColumn();
    var table = new BSTable(Classroom.id, "/classroom/list", defaultColunms);
    table.setPaginationType("client");
    Classroom.table = table.init();
});
