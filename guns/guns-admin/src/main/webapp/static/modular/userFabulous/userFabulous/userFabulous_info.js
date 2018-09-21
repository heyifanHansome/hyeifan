/**
 * 初始化用户点赞详情对话框
 */
var UserFabulousInfoDlg = {
    userFabulousInfoData : {}
};

/**
 * 清除数据
 */
UserFabulousInfoDlg.clearData = function() {
    this.userFabulousInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserFabulousInfoDlg.set = function(key, val) {
    this.userFabulousInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserFabulousInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserFabulousInfoDlg.close = function() {
    parent.layer.close(window.parent.UserFabulous.layerIndex);
}

/**
 * 收集数据
 */
UserFabulousInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('columnId')
    .set('worksId')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
UserFabulousInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userFabulous/add", function(data){
        Feng.success("添加成功!");
        window.parent.UserFabulous.table.refresh();
        UserFabulousInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userFabulousInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
UserFabulousInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userFabulous/update", function(data){
        Feng.success("修改成功!");
        window.parent.UserFabulous.table.refresh();
        UserFabulousInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userFabulousInfoData);
    ajax.start();
}

$(function() {

});
