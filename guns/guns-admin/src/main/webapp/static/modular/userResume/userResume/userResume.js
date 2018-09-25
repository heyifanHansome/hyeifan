/**
 * 用户简历管理管理初始化
 */
var UserResume = {
    id: "UserResumeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserResume.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '自增主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '用户', field: 'Pname', visible: false, align: 'center', valign: 'middle'},
            {title: '姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'PapplyStatus', visible: true, align: 'center', valign: 'middle'},
            {title: '性别', field: 'Psex', visible: true, align: 'center', valign: 'middle'},
            {title: '会员生日', field: 'birthday', visible: true, align: 'center', valign: 'middle'},
            {title: '工龄', field: 'work_time', visible: true, align: 'center', valign: 'middle'},
            {title: '微信号', field: 'wechat_id', visible: true, align: 'center', valign: 'middle'},
            {title: '手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '邮箱', field: 'email', visible: true, align: 'center', valign: 'middle'},
            {title: '优势', field: 'advantage', visible: true, align: 'center', valign: 'middle'},
            {title: '无序列表', field: 'info', visible: false, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'create_time', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'update_time', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
UserResume.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserResume.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户简历管理
 */
UserResume.openAddUserResume = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户简历管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userResume/userResume_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户简历管理详情
 */
UserResume.openUserResumeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户简历管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userResume/userResume_update/' + UserResume.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户简历管理
 */
UserResume.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/userResume/delete", function (data) {
            Feng.success("删除成功!");
            UserResume.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userResumeId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询用户简历管理列表
 */
UserResume.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    UserResume.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserResume.initColumn();
    var table = new BSTable(UserResume.id, "/userResume/list", defaultColunms);
    table.setPaginationType("client");
    UserResume.table = table.init();
});
