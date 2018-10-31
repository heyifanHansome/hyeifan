/**
 * 初始化会员等级评价指标详情对话框
 */
var TargetInfoDlg = {
    targetInfoData : {}
};

/**
 * 清除数据
 */
TargetInfoDlg.clearData = function() {
    this.targetInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TargetInfoDlg.set = function(key, val) {
    this.targetInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TargetInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
TargetInfoDlg.close = function() {
    parent.layer.close(window.parent.Target.layerIndex);
}

/**
 * 收集数据
 */
TargetInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('grade')
    .set('score')
    .set('pid');
}

/**
 * 提交添加
 */
TargetInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/target/add", function(data){
        Feng.success("添加成功!");
        window.parent.Target.table.refresh();
        TargetInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.targetInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
TargetInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/target/update", function(data){
        Feng.success("修改成功!");
        window.parent.Target.table.refresh();
        TargetInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.targetInfoData);
    ajax.start();
}

/**
 * 点击父级编号input框时
 */
TargetInfoDlg.onClickDept = function (e, treeId, treeNode) {
    $("#pidName").attr("value", TargetInfoDlg.ztreeInstance.getSelectedVal());
    $("#pid").attr("value", treeNode.id);
};


/**
 * 显示父级菜单选择的树
 */
TargetInfoDlg.showMenuSelectTree = function () {
    Feng.showInputTree("pid", "pidTreeDiv", 15, 34);
};

$(function() {
    Feng.initValidator("menuInfoForm", TargetInfoDlg.validateFields);

    var ztree = new $ZTree("pidTree", "/target/selectTargetTreeList");
    ztree.bindOnClick(TargetInfoDlg.onClickDept);
    ztree.init();
    console.log(ztree)
    TargetInfoDlg.ztreeInstance = ztree;
});
