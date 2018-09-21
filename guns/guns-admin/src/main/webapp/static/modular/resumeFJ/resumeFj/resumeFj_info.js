/**
 * 初始化简历附件详情对话框
 */
var ResumeFjInfoDlg = {
    resumeFjInfoData : {}
};

/**
 * 清除数据
 */
ResumeFjInfoDlg.clearData = function() {
    this.resumeFjInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ResumeFjInfoDlg.set = function(key, val) {
    this.resumeFjInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ResumeFjInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ResumeFjInfoDlg.close = function() {
    parent.layer.close(window.parent.ResumeFj.layerIndex);
}

/**
 * 收集数据
 */
ResumeFjInfoDlg.collectData = function() {
    this
    .set('id')
    .set('resumeId')
    .set('name')
    .set('url')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
ResumeFjInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/resumeFj/add", function(data){
        Feng.success("添加成功!");
        window.parent.ResumeFj.table.refresh();
        ResumeFjInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.resumeFjInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ResumeFjInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/resumeFj/update", function(data){
        Feng.success("修改成功!");
        window.parent.ResumeFj.table.refresh();
        ResumeFjInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.resumeFjInfoData);
    ajax.start();
}

$(function() {

});
