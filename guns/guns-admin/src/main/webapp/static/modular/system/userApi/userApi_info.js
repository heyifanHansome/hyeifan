/**
 * 初始化详情对话框
 */
var UserApiInfoDlg = {
    userApiInfoData : {}
};

/**
 * 清除数据
 */
UserApiInfoDlg.clearData = function() {
    this.userApiInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserApiInfoDlg.set = function(key, val) {
    this.userApiInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserApiInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserApiInfoDlg.close = function() {
    parent.layer.close(window.parent.UserApi.layerIndex);
}

/**
 * 收集数据
 */
UserApiInfoDlg.collectData = function() {
    this
    .set('id')
    .set('avatar')
    .set('name')
    .set('birthday')
    .set('sex')
    .set('phone')
    .set('createtime');
}

/**
 * 提交添加
 */
UserApiInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userLoginApi/add", function(data){
        Feng.success("添加成功!");
        window.parent.UserApi.table.refresh();
        UserApiInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userApiInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
UserApiInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userLoginApi/update", function(data){
        Feng.success("修改成功!");
        window.parent.UserApi.table.refresh();
        UserApiInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userApiInfoData);
    ajax.start();
}

$(function() {
    // 初始化缩略图上传
    var avatarUp = new $WebUpload("avatar","/tool/uploadFile");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init("/tool/uploadFile");

});
