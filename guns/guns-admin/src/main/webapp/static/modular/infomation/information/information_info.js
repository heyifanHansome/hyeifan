/**
 * 初始化资讯管理详情对话框
 */
var InformationInfoDlg = {
    informationInfoData : {}
};

/**
 * 清除数据
 */
InformationInfoDlg.clearData = function() {
    this.informationInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InformationInfoDlg.set = function(key, val) {
    this.informationInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InformationInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
InformationInfoDlg.close = function() {
    parent.layer.close(window.parent.Information.layerIndex);
}

/**
 * 收集数据
 */
InformationInfoDlg.collectData = function() {
    this
    .set('id')
    .set('columnId')
    .set('title')
    .set('thumb')
    .set('images')
    .set('url')
    .set('description')
    .set('cityId')
    .set('sourceId')
    .set('uid')
    .set('publishIp')
    .set('content')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
InformationInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/information/add", function(data){
        Feng.success("添加成功!");
        window.parent.Information.table.refresh();
        InformationInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.informationInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
InformationInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/information/update", function(data){
        Feng.success("修改成功!");
        window.parent.Information.table.refresh();
        InformationInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.informationInfoData);
    ajax.start();
}

$(function() {

});
