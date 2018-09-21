/**
 * 初始化厨师收徒标准详情对话框
 */
var EnlighteningStandardInfoDlg = {
    enlighteningStandardInfoData : {}
};

/**
 * 清除数据
 */
EnlighteningStandardInfoDlg.clearData = function() {
    this.enlighteningStandardInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
EnlighteningStandardInfoDlg.set = function(key, val) {
    this.enlighteningStandardInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
EnlighteningStandardInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
EnlighteningStandardInfoDlg.close = function() {
    parent.layer.close(window.parent.EnlighteningStandard.layerIndex);
}

/**
 * 收集数据
 */
EnlighteningStandardInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('gradeId')
    .set('remark')
    .set('birthday')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
EnlighteningStandardInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/enlighteningStandard/add", function(data){
        Feng.success("添加成功!");
        window.parent.EnlighteningStandard.table.refresh();
        EnlighteningStandardInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.enlighteningStandardInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
EnlighteningStandardInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/enlighteningStandard/update", function(data){
        Feng.success("修改成功!");
        window.parent.EnlighteningStandard.table.refresh();
        EnlighteningStandardInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.enlighteningStandardInfoData);
    ajax.start();
}

$(function() {

});
