/**
 * 初始化系统设置详情对话框
 */
var SettingInfoDlg = {
    settingInfoData : {}
};

/**
 * 清除数据
 */
SettingInfoDlg.clearData = function() {
    this.settingInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SettingInfoDlg.set = function(key, val) {
    this.settingInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SettingInfoDlg.get = function(key) {
    return $("#" + key).val();
}


/**
 * 收集数据
 */
SettingInfoDlg.collectData = function() {
    this
    .set('id')
    .set('aliSafrvCertCheckAppcode')
    .set('aliOssEndpoint')
    .set('aliOssBucket')
    .set('aliOssAccessKey')
    .set('aliOssAccessId')
    .set('aliOssImgPath')
    .set('aliOssFilePath');
}


/**
 * 提交修改
 */
SettingInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/setting/update", function(data){
        Feng.success("修改成功!");

        // window.parent.Setting.table.refresh();
        // SettingInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.settingInfoData);
    ajax.start();
}

$(function() {

});
