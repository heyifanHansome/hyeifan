/**
 * 收徒详情管理初始化
 */
var Rule = {
    id: "RuleTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Rule.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
        {title: '创建人', field: 'createBy', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Rule.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Rule.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加收徒详情
 */
Rule.openAddRule = function () {
    var index = layer.open({
        type: 2,
        title: '添加收徒详情',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/rule/rule_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看收徒详情详情
 */
Rule.openRuleDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '收徒详情详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/rule/rule_update/' + Rule.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除收徒详情
 */
Rule.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/rule/delete", function (data) {
            Feng.success("删除成功!");
            Rule.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("ruleId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询收徒详情列表
 */
Rule.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Rule.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Rule.initColumn();
    var table = new BSTable(Rule.id, "/rule/list", defaultColunms);
    table.setPaginationType("client");
    Rule.table = table.init();
});
