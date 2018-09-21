/**
 * 初始化用户资历详情对话框
 */
var ResumeLiveInfoDlg = {
    resumeLiveInfoData : {}
};

/**
 * 清除数据
 */
ResumeLiveInfoDlg.clearData = function() {
    this.resumeLiveInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ResumeLiveInfoDlg.set = function(key, val) {
    this.resumeLiveInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ResumeLiveInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ResumeLiveInfoDlg.close = function() {
    parent.layer.close(window.parent.ResumeLive.layerIndex);
}

/**
 * 收集数据
 */
ResumeLiveInfoDlg.collectData = function() {
    this
    .set('id')
    .set('resumeId')
    .set('company')
    .set('positionName')
    .set('startTime')
    .set('endTime')
    .set('remark')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
ResumeLiveInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/resumeLive/add", function(data){
        Feng.success("添加成功!");
        window.parent.ResumeLive.table.refresh();
        ResumeLiveInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.resumeLiveInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ResumeLiveInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/resumeLive/update", function(data){
        Feng.success("修改成功!");
        window.parent.ResumeLive.table.refresh();
        ResumeLiveInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.resumeLiveInfoData);
    ajax.start();
}

$(function() {

});
