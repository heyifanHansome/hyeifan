/**
 * 会员等级评价指标管理初始化
 */
var Target = {
    id: "TargetTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Target.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: 'ID', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '等级', field: 'grade', visible: true, align: 'center', valign: 'middle'},
            {title: '分值', field: 'score', visible: true, align: 'center', valign: 'middle'},
            // {title: '父ID(顶级为0)', field: 'pid', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Target.check = function () {
    // var selected = $('#' + this.id).bootstrapTable('getSelections');
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Target.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员等级评价指标
 */
Target.openAddTarget = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员等级评价指标',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/target/target_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员等级评价指标详情
 */
Target.openTargetDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员等级评价指标详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            // content: Feng.ctxPath + '/target/target_update/' + Target.seItem.id
            content: Feng.ctxPath + '/target/target_update/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员等级评价指标
 */
Target.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/target/delete", function (data) {
            Feng.success("删除成功!");
            Target.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("targetId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员等级评价指标列表
 */
Target.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Target.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Target.initColumn();

    // var table = new BSTable(Target.id, "/target/list", defaultColunms);
    // table.setPaginationType("client");

    var table = new BSTreeTable(Target.id, "/target/list", defaultColunms);
    table.setExpandColumn(1);//设置在哪一列上面显示展开按钮,从0开始
    table.setIdField("id");//设置记录返回的id值
    table.setCodeField("id");//设置记录分级的字段
    table.setParentCodeField("pid");//设置记录分级的父级字段
    table.setExpandAll(true);//设置是否默认全部展开
    table.init();
    Target.table = table;
});
